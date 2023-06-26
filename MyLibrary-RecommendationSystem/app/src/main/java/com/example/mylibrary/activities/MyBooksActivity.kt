package com.example.mylibrary.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylibrary.R
import com.example.mylibrary.adapters.BookItemsAdapter
import com.example.mylibrary.database.RealTimeDataBase
import com.example.mylibrary.models.Book
import com.example.mylibrary.utils.Constants
import com.example.mylibrary.utils.SwipeToDeleteCallback
import com.example.mylibrary.utils.SwipeToEditCallback

class MyBooksActivity : BaseActivity() {

    companion object {
        const val MY_BOOK_CODE: Int = 7
    }

    private lateinit var adapter: BookItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_books)

        // Hide the top bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Set up the action bar
        setupActionBar()

        // Get the books for the user
        getBooks()
    }

    /**
     * Sets up the action bar for the page with back button
     */
    private fun setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar_my_books))
        findViewById<Toolbar>(R.id.toolbar_my_books).setBackgroundColor(resources.getColor(R.color.purple_200))
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_back)
            actionBar.title = resources.getString(R.string.my_books)
        }
        findViewById<Toolbar>(R.id.toolbar_my_books).setNavigationOnClickListener {
            onBackPressed()
        }
    }

    /**
     * If there are books in the database, it populates the list.
     * Has a swipe right to edit the book and a swipe left to delete the book.
     * It also has the search bar for the list and the click listener for each book item.
     */
    fun populatesBooksList(bookList: ArrayList<Book>) {
        makeProgressDialogInvisible()
        val rvBookList = findViewById<RecyclerView>(R.id.rv_books_list)
        if (bookList.size > 0) {
            rvBookList.visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_no_books_in_collection).visibility = View.GONE

            rvBookList.layoutManager = LinearLayoutManager(this)
            findViewById<RecyclerView>(R.id.rv_books_list).setHasFixedSize(true)

            adapter = BookItemsAdapter(this, bookList)
            rvBookList.adapter = adapter

            // Swipe for edit book
            val editSwipeHandler = object : SwipeToEditCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val book = adapter.getBookAt(position)
                    book?.let {
                        adapter.notifyEditItem(this@MyBooksActivity, it, MY_BOOK_CODE)
                    }
                }
            }

            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(findViewById(R.id.rv_books_list))

            // Swipe for delete book
            val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    adapter.removeAt(this@MyBooksActivity, viewHolder.adapterPosition)
                }
            }

            val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
            deleteItemTouchHelper.attachToRecyclerView(findViewById(R.id.rv_books_list))

            // Search bar
            val searchView = findViewById<SearchView>(R.id.search_my_books)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    val searchList = ArrayList<Book>()
                    for (book in bookList) {
                        if (book.title.lowercase().contains(newText.lowercase()) ||
                            book.author.lowercase().contains(newText.lowercase()) ||
                            book.genre.lowercase().contains(newText.lowercase()) ||
                            book.isbn.lowercase().contains(newText.lowercase())
                        ) {
                            searchList.add(book)
                        }
                    }
                    adapter.searchBookList(searchList)
                    return true
                }
            })

            // Click on item to go to info page
            adapter.setOnClickListener(object : BookItemsAdapter.OnClickListener {
                override fun onClick(position: Int, model: Book) {
                    val intent = Intent(this@MyBooksActivity, BookInfoActivity::class.java)
                    intent.putExtra(Constants.DOCUMENT_ID, model.documentId)
                    startActivity(intent)
                }
            })
        } else {
            rvBookList.visibility = View.GONE
            findViewById<TextView>(R.id.tv_no_books_in_collection).visibility = View.VISIBLE
        }
    }

    /**
     * Gets the books from the database for the current user
     */
    private fun getBooks() {
        makeProgressDialogVisible(resources.getString(R.string.please_wait))
        RealTimeDataBase().getBookList(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MY_BOOK_CODE) {
            getBooks()
        }
    }

    fun bookUpdateSuccess() {
        makeProgressDialogInvisible()
        setResult(Activity.RESULT_OK)
    }
}