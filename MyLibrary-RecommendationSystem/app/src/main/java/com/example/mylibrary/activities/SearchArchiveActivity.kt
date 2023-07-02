package com.example.mylibrary.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylibrary.R
import com.example.mylibrary.adapters.ArchiveItemsAdapter
import com.example.mylibrary.adapters.BookItemsAdapter
import com.example.mylibrary.database.RealTimeDataBase
import com.example.mylibrary.models.Archive
import com.example.mylibrary.models.Book
import com.example.mylibrary.utils.Constants
import com.example.mylibrary.utils.SwipeToAddCallback
import com.example.mylibrary.utils.SwipeToDeleteCallback
import com.example.mylibrary.utils.SwipeToEditCallback

class SearchArchiveActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_archive)

        //hide the top bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //sets up action bar
        setupActionBar()

        //get the books from archive
        getArchive()
    }

    /**
     * Sets up the action bar for the page with back button
     */
    private fun setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar_archive))
        //get toolbar id
        findViewById<Toolbar>(R.id.toolbar_archive).setBackgroundColor(resources.getColor(R.color.purple_200))
        val actionBar = supportActionBar
        //change the title and add back button with icon
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_back)
            actionBar.title = resources.getString(R.string.archive)
        }
        //set the back button
        findViewById<Toolbar>(R.id.toolbar_archive).setNavigationOnClickListener {
            onBackPressed()
        }
    }

    /**
     * populates the archive page with the books from the realtime database
     */
    fun populatesArchiveList(bookList: ArrayList<Archive>) {
        makeProgressDialogInvisible()
        val rvBookList = findViewById<RecyclerView>(R.id.rv_archive_list)
        //if there are books in the archive table populates the list on the page
        if (bookList.size > 0) {
            //make the book list visible
            rvBookList.visibility = View.VISIBLE
            //make the default message invisible
            findViewById<TextView>(R.id.tv_no_archive).visibility = View.GONE

            // creates a layout for the recyclerView
            rvBookList.layoutManager = LinearLayoutManager(this)
            findViewById<RecyclerView>(R.id.rv_archive_list).setHasFixedSize(true)

            // creates an adapter for the recyclerView
            val adapter = ArchiveItemsAdapter(this, bookList)
            rvBookList.adapter = adapter

            //swiper to add the book to my books collection
            val addSwipeHandler = object : SwipeToAddCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    adapter.notifyAddItem(this@SearchArchiveActivity, viewHolder.adapterPosition)

                }
            }

            val addItemTouchHelper = ItemTouchHelper(addSwipeHandler)
            addItemTouchHelper.attachToRecyclerView(findViewById(R.id.rv_archive_list))

            //search bar
            findViewById<SearchView>(R.id.search_archive).setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                //search book by title, author, genre or isbn
                override fun onQueryTextChange(newText: String): Boolean {
                    val searchList = ArrayList<Archive>()
                    for (book in bookList) {
                        if (book.title?.lowercase()?.contains(newText.lowercase()) == true) {
                            searchList.add(book)
                        } else if (book.author?.lowercase()
                                ?.contains(newText.lowercase()) == true
                        ) {
                            searchList.add(book)
                        } else if (book.genre?.lowercase()?.contains(newText.lowercase()) == true) {
                            searchList.add(book)
                        } else if (book.isbn?.lowercase()?.contains(newText.lowercase()) == true) {
                            searchList.add(book)
                        }
                    }
                    adapter.searchBookList(searchList)
                    return true
                }
            })

            //click item for info about the book
            adapter.setOnClickListener(object :
                ArchiveItemsAdapter.OnClickListener {
                override fun onClick(position: Int, model: Archive) {
                    val intent = Intent(this@SearchArchiveActivity, ArchiveInfoActivity::class.java)
                    intent.putExtra(Constants.ISBN, model.isbn)
                    startActivity(intent)
                }
            })
        } else {
            rvBookList.visibility = View.GONE
            findViewById<TextView>(R.id.tv_no_archive).visibility = View.VISIBLE
        }
    }

    /**
     * gets the archive data from realtime database
     */
    private fun getArchive() {
        makeProgressDialogVisible(resources.getString(R.string.please_wait))
        RealTimeDataBase().getArchiveList(this, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MyBooksActivity.MY_BOOK_CODE) {
            getArchive()
        }
    }

}