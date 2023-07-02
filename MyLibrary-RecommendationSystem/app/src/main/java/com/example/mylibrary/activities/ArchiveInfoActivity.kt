package com.example.mylibrary.activities

import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.mylibrary.R
import com.example.mylibrary.database.RealTimeDataBase
import com.example.mylibrary.models.Archive
import com.example.mylibrary.utils.Constants

class ArchiveInfoActivity : BaseActivity() {
    private var isbn: String = ""
    private lateinit var archiveItem: Archive

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_info)

        //gets the isbn from the other activity
        if (intent.hasExtra(Constants.ISBN)) {
            isbn = intent.getStringExtra(Constants.ISBN)!!
        }

        //make progress bar visible
        makeProgressDialogVisible(resources.getString(R.string.please_wait))

        //get the book details for that specific book
        RealTimeDataBase().loadArchiveData(this, isbn)

        //hide the progress bar
        makeProgressDialogInvisible()
    }

    /**
     * Sets up the action bar for the page with back button
     */
    private fun setupActionBar(title: String) {
        setSupportActionBar(findViewById(R.id.toolbar_archive_info))
        //get toolbar id
        findViewById<Toolbar>(R.id.toolbar_archive_info).setBackgroundColor(resources.getColor(R.color.purple_200))
        val actionBar = supportActionBar
        //change the title and add back button with icon
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_back)
            actionBar.title = title
        }
        //set the back button
        findViewById<Toolbar>(R.id.toolbar_archive_info).setNavigationOnClickListener {
            onBackPressed()
        }
    }

    /**
     * Gets data from data base and put it on the page
     */
    fun setArchiveData(archive: Archive) {
        archiveItem = archive
        //put the title in the toolbar as the title of the book
        setupActionBar(archive.title)
        //put in the image element the image from the book in archive
        Glide
            .with(this@ArchiveInfoActivity)
            .load(archive.image)
            .centerCrop()
            .placeholder(R.drawable.ic_book)
            .into(findViewById(R.id.iv_archive_info))

        //put the rest of details about the book in the elements on the page
        findViewById<TextView>(R.id.tv_archive_title).text = archive.title
        findViewById<TextView>(R.id.tv_archive_author).text = archive.author
        findViewById<RatingBar>(R.id.tv_archive_rating).rating = archive.rating.toFloat()
        findViewById<TextView>(R.id.tv_archive_description).text = archive.description
        findViewById<TextView>(R.id.tv_archive_genre).text = archive.genre
        findViewById<TextView>(R.id.tv_archive_pages).text = archive.pages
        findViewById<TextView>(R.id.tv_archive_isbn).text = archive.isbn
        findViewById<TextView>(R.id.tv_archive_publish_year).text = archive.publishedYear

    }
}