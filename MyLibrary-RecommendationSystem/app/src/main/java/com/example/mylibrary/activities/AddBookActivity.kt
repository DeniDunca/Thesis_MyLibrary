package com.example.mylibrary.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.mylibrary.R
import com.example.mylibrary.database.RealTimeDataBase
import com.example.mylibrary.models.Book
import com.example.mylibrary.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class AddBookActivity : BaseActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var userId: String
    private var bookImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        //hide the top bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //set the action bar
        setupActionBar()

        //gets the user id from the other activity
        if (intent.hasExtra(Constants.USERID)) {
            userId = intent.getStringExtra(Constants.USERID)!!
        }

        //when clicked on add book, it checks for permissions
        findViewById<ImageView>(R.id.iv_add_book).setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageChooser(this)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_CODE
                )
            }
        }

        //when click on add book to collection uploads the book image to storage and the book details to firestore
        findViewById<Button>(R.id.btn_add_book_to_collection).setOnClickListener {
            if (selectedImageUri != null) {
                uploadBookImage()
            } else {
                makeProgressDialogVisible(resources.getString(R.string.please_wait))
                createBook()
            }
        }
    }

    /**
     * Sets up the action bar for the page with back button
     */
    private fun setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar_add_book))
        //get toolbar id
        findViewById<Toolbar>(R.id.toolbar_add_book).setBackgroundColor(resources.getColor(R.color.purple_200))
        val actionBar = supportActionBar
        //change the title and add back button with icon
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_back)
            actionBar.title = resources.getString(R.string.add_book_title)
        }
        //set the back button
        findViewById<Toolbar>(R.id.toolbar_add_book).setNavigationOnClickListener {
            onBackPressed()
        }
    }

    /**
     * Asks for permission and if is denied shows a message
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //verifies if the mobile has permission
        if (requestCode == Constants.READ_STORAGE_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {//if it doesn't have show message
                Toast.makeText(
                    this,
                    "Please allow storage permission from the app settings!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    /**
     * gets the selected image from gallery and places it in the image book holder
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //get the image from the storage and put it in the element using Glide library
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_CODE && data!!.data != null) {
            selectedImageUri = data.data
            try {
                Glide.with(this).load(selectedImageUri).centerCrop().placeholder(R.drawable.ic_book)
                    .into(findViewById(R.id.iv_add_book))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * creates a book with the data from the form from interface
     */
    private fun createBook() {
        //get the inputs written by user
        val title: String =
            findViewById<EditText>(R.id.et_add_book_title).text.toString().trim { it <= ' ' }
        val author: String =
            findViewById<EditText>(R.id.et_add_book_author).text.toString().trim { it <= ' ' }
        val description: String =
            findViewById<EditText>(R.id.et_add_book_description).text.toString().trim { it <= ' ' }
        val isbn: String =
            findViewById<EditText>(R.id.et_add_book_isbn).text.toString().trim { it <= ' ' }
        val genre: String =
            findViewById<EditText>(R.id.et_add_book_genre).text.toString().trim { it <= ' ' }
        val pages: String =
            findViewById<EditText>(R.id.et_add_book_pages).text.toString().trim { it <= ' ' }
        val publishedYear: String =
            findViewById<EditText>(R.id.et_add_book_publish_year).text.toString().trim { it <= ' ' }

        //validate the inputs
        if (validateForm(title, author, description, isbn, genre, pages, publishedYear)) {
            val book = Book(
                bookImageURL,
                title,
                author,
                description,
                isbn,
                genre,
                pages,
                publishedYear
            )
            //add the created book to database
            RealTimeDataBase().createBookAndAddItToUser(this, book)
        } else {
            makeProgressDialogInvisible()
        }
    }

    /**
     * Upload to storage the images
     */
    private fun uploadBookImage() {
        makeProgressDialogVisible(resources.getString(R.string.please_wait))
        //if an image was added, put it intro the Storage in Database
        if (selectedImageUri != null) {
            val ref: StorageReference = FirebaseStorage.getInstance().reference.child(
                "Book_image" + System.currentTimeMillis() + "." + Constants.getFileExtension(
                    this, selectedImageUri
                )
            )
            ref.putFile(selectedImageUri!!).addOnSuccessListener { taskSnapshot ->
                Log.e(
                    "Firebase image url", taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    Log.e("Downloadable image url", uri.toString())
                    bookImageURL = uri.toString()
                    createBook()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this@AddBookActivity, e.message, Toast.LENGTH_LONG).show()
                makeProgressDialogInvisible()
            }
        }
    }

    /**
     * Add book to fireStore with the image from storage
     */
    fun bookCreatedSuccessfully() {
        makeProgressDialogInvisible()
        finish()
    }

    /**
     * Function that verifies if the fields of the add book form are not empty
     */
    private fun validateForm(
        title: String,
        author: String,
        description: String,
        ISBN: String,
        genre: String,
        pages: String,
        publishedYear: String,

        ): Boolean {
        return when {
            TextUtils.isEmpty(title) -> {
                displayError("Please enter the book title")
                false
            }

            TextUtils.isEmpty(author) -> {
                displayError("Please enter the book author")
                false
            }

            TextUtils.isEmpty(description) -> {
                displayError("Please enter the book description")
                false
            }

            TextUtils.isEmpty(ISBN) -> {
                displayError("Please enter the book ISBN")
                false
            }

            TextUtils.isEmpty(genre) -> {
                displayError("Please enter the book genre")
                false
            }

            TextUtils.isEmpty(pages) -> {
                displayError("Please enter the book number of pages")
                false
            }

            TextUtils.isEmpty(publishedYear) -> {
                displayError("Please enter the book published year")
                false
            }

            else -> {
                true
            }
        }
    }
}