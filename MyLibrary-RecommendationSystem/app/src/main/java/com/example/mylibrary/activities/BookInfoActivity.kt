package com.example.mylibrary.activities

import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.mylibrary.R
import com.example.mylibrary.database.RealTimeDataBase
import com.example.mylibrary.models.Book
import com.example.mylibrary.utils.Constants

class BookInfoActivity : BaseActivity() {
    private var bookId: String = ""
    private lateinit var bookDetails: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_info)

        //gets the book id from the other activity
        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
            bookId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }

        //make progress bar visible
        makeProgressDialogVisible(resources.getString(R.string.please_wait))

        //get the book details for that specific book
        RealTimeDataBase().loadBookData(this, bookId)

        //hide the progress bar
        makeProgressDialogInvisible()
    }

    /**
     * Sets up the action bar for the page with back button
     */
    private fun setupActionBar(title: String) {
        setSupportActionBar(findViewById(R.id.toolbar_book_info))
        //get toolbar id
        findViewById<Toolbar>(R.id.toolbar_book_info).setBackgroundColor(resources.getColor(R.color.purple_200))
        val actionBar = supportActionBar
        //change the title and add back button with icon
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_back)
            actionBar.title = title
        }
        //set the back button
        findViewById<Toolbar>(R.id.toolbar_book_info).setNavigationOnClickListener {
            onBackPressed()
        }
    }

    /**
     * Gets data from data base and put it on the page
     */
    fun setBookData(book: Book) {
        bookDetails = book
        //put the title in the toolbar as the title of the book
        setupActionBar(book.title)
        //put in the image element the image from the book in archive
        Glide
            .with(this@BookInfoActivity)
            .load(book.image)
            .centerCrop()
            .placeholder(R.drawable.ic_book)
            .into(findViewById(R.id.iv_book_info))

        //put the rest of details about the book in the elements on the page
        findViewById<TextView>(R.id.tv_book_title).text = book.title
        findViewById<TextView>(R.id.tv_book_author).text = book.author
        if (book.rating.isNotEmpty()) {
            findViewById<RatingBar>(R.id.tv_book_rating).rating = book.rating.toFloat()
        } else {
            findViewById<RatingBar>(R.id.tv_book_rating).rating = book.myRate
        }
        findViewById<TextView>(R.id.tv_book_description).text = book.description
        findViewById<TextView>(R.id.tv_book_genre).text = book.genre
        findViewById<TextView>(R.id.tv_book_pages).text = book.pages
        findViewById<TextView>(R.id.tv_book_isbn).text = book.isbn
        findViewById<TextView>(R.id.tv_book_publish_year).text = book.publishedYear

    }

}