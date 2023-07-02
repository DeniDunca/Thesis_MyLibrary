package com.example.mylibrary.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UpdateBookActivity : BaseActivity() {

    private var selectedImageUri: Uri? = null
    private var bookImageUrl: String = ""
    private lateinit var bookDetails: Book
    private var bookId: String = ""
    private var calendar = Calendar.getInstance()
    private lateinit var dateSetListenerStartDate: DatePickerDialog.OnDateSetListener
    private lateinit var dateSetListenerFinishDate: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_book)

        //set up action bar
        setupActionBar()

        //gets the book id from the other activity
        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
            bookId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }

        //make progress bar visible
        makeProgressDialogVisible(resources.getString(R.string.please_wait))


        //creates a date picker for start reading date and sets the chosen date
        dateSetListenerStartDate =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate(findViewById(R.id.et_add_start_reading_date))
            }

        //on clink on start reading field that it opens a picker
        findViewById<EditText>(R.id.et_add_start_reading_date).setOnClickListener {
            DatePickerDialog(
                this@UpdateBookActivity,
                dateSetListenerStartDate,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //creates a date picker for finish reading date and sets the chosen date
        dateSetListenerFinishDate =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate(findViewById(R.id.et_add_finish_reading_date))
            }

        //on clink on finish reading field that it opens a picker
        findViewById<EditText>(R.id.et_add_finish_reading_date).setOnClickListener {
            DatePickerDialog(
                this@UpdateBookActivity,
                dateSetListenerFinishDate,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //get the book details for that specific book
        RealTimeDataBase().loadBookData(this, bookId)

        //hide the progress bar
        makeProgressDialogInvisible()

        //checking permissions, if it has rights than open image chooser
        findViewById<ImageView>(R.id.iv_update_book).setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
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

        //on button click update the image
        findViewById<Button>(R.id.btn_update_book).setOnClickListener {
            if (selectedImageUri != null) {
                uploadImage()
            } else {
                makeProgressDialogVisible(resources.getString(R.string.please_wait))
                updateBookDetails()
            }
        }
    }

    /**
     * Sets up the action bar for the page with back button
     */
    private fun setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar_update_book))
        //get toolbar id
        findViewById<Toolbar>(R.id.toolbar_update_book).setBackgroundColor(resources.getColor(R.color.purple_200))
        val actionBar = supportActionBar
        //change the title and add back button with icon
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_back)
            actionBar.title = resources.getString(R.string.update_book_title)
        }
        //set the back button
        findViewById<Toolbar>(R.id.toolbar_update_book).setNavigationOnClickListener {
            customDialogForBackButton()
        }
    }

    /**
     * Asks for permission and if is denied shows a message
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //verifies if the user has given permission to access the storage for add book image
        if (requestCode == Constants.READ_STORAGE_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                //if the app doesn't have the permission display message
                Toast.makeText(
                    this,
                    "Please allow storage permission from the app settings!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //put the image from the database to the image book element using Glide
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_CODE && data!!.data != null) {
            selectedImageUri = data.data
            try {
                Glide
                    .with(this@UpdateBookActivity)
                    .load(selectedImageUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_book)
                    .into(findViewById(R.id.iv_update_book))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Gets data from data base and put it on the page
     */
    fun setBookData(book: Book) {
        bookDetails = book
        //set the chosen image using glide
        Glide
            .with(this@UpdateBookActivity)
            .load(book.image)
            .centerCrop()
            .placeholder(R.drawable.ic_book)
            .into(findViewById(R.id.iv_update_book))

        //set the book details
        findViewById<EditText>(R.id.et_add_book_title).setText(book.title)
        findViewById<EditText>(R.id.et_add_book_author).setText(book.author)
        findViewById<RatingBar>(R.id.et_add_book_rate).rating = book.myRate
        findViewById<EditText>(R.id.et_add_book_pages_read).setText(book.pagesRead)
        findViewById<EditText>(R.id.et_add_start_reading_date).setText(book.startDate)
        findViewById<EditText>(R.id.et_add_finish_reading_date).setText(book.finishDate)
        findViewById<EditText>(R.id.et_add_book_notes).setText(book.notes)
        findViewById<EditText>(R.id.et_add_book_description).setText(book.description)
        findViewById<EditText>(R.id.et_add_book_isbn).setText(book.isbn)
        findViewById<EditText>(R.id.et_add_book_genre).setText(book.genre)
        findViewById<EditText>(R.id.et_add_book_pages).setText(book.pages)
        findViewById<EditText>(R.id.et_add_book_publish_year).setText(book.publishedYear)

        //the user cannot modify the details about the books added from archive, only the details from the books added manually
        if (book.rating.isNotEmpty()) {
            findViewById<EditText>(R.id.et_add_book_title).isFocusable = false
            findViewById<EditText>(R.id.et_add_book_title).isClickable = false

            findViewById<EditText>(R.id.et_add_book_author).isFocusable = false
            findViewById<EditText>(R.id.et_add_book_author).isClickable = false

            findViewById<EditText>(R.id.et_add_book_pages).isFocusable = false
            findViewById<EditText>(R.id.et_add_book_pages).isClickable = false

            findViewById<EditText>(R.id.et_add_book_genre).isFocusable = false
            findViewById<EditText>(R.id.et_add_book_genre).isClickable = false

            findViewById<EditText>(R.id.et_add_book_publish_year).isFocusable = false
            findViewById<EditText>(R.id.et_add_book_publish_year).isClickable = false

            findViewById<EditText>(R.id.et_add_book_isbn).isFocusable = false
            findViewById<EditText>(R.id.et_add_book_isbn).isClickable = false

            findViewById<EditText>(R.id.et_add_book_description).isFocusable = false
            findViewById<EditText>(R.id.et_add_book_description).isClickable = false

        }
    }

    /**
     * upload to storage the images
     */
    private fun uploadImage() {
        makeProgressDialogVisible(resources.getString(R.string.please_wait))
        //gets the image and uploads it to the Storage database in Firebase
        if (selectedImageUri != null) {
            val ref: StorageReference = FirebaseStorage.getInstance().reference.child(
                "Book_image" + System.currentTimeMillis() + "." + Constants.getFileExtension(
                    this,
                    selectedImageUri
                )
            )
            ref.putFile(selectedImageUri!!).addOnSuccessListener { taskSnapshot ->
                Log.e(
                    "Firebase image url",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    Log.e("Downloadable image url", uri.toString())
                    bookImageUrl = uri.toString()
                    updateBookDetails()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this@UpdateBookActivity, e.message, Toast.LENGTH_LONG).show()
                makeProgressDialogInvisible()
            }
        }
    }

    /**
     * Update the book from fireStore with the image from storage
     */
    fun bookUpdateSuccess() {
        makeProgressDialogInvisible()
        setResult(Activity.RESULT_OK)
        finish()
    }

    /**
     * Verifies the modifications from the update book activity and calls the firebase function to change them in the database
     */
    private fun updateBookDetails() {
        val bookHashMap = HashMap<String, Any>()

        //if the image is empty or is the same as the one in database we do no changes
        if (bookImageUrl.isNotEmpty() && bookImageUrl != bookDetails.image) {
            bookHashMap[Constants.IMAGE] = bookImageUrl
        }
        //update all the book details
        if (findViewById<EditText>(R.id.et_add_book_title).text.toString() != bookDetails.title) {
            bookHashMap[Constants.TITLE] =
                findViewById<EditText>(R.id.et_add_book_title).text.toString()
        }
        if (findViewById<EditText>(R.id.et_add_book_author).text.toString() != bookDetails.author) {
            bookHashMap[Constants.AUTHOR] =
                findViewById<EditText>(R.id.et_add_book_author).text.toString()
        }
        if (findViewById<EditText>(R.id.et_add_book_description).text.toString() != bookDetails.description) {
            bookHashMap[Constants.DESCRIPTION] =
                findViewById<EditText>(R.id.et_add_book_description).text.toString()
        }
        if (findViewById<EditText>(R.id.et_add_book_isbn).text.toString() != bookDetails.isbn) {
            bookHashMap[Constants.ISBN] =
                findViewById<EditText>(R.id.et_add_book_isbn).text.toString()
        }
        if (findViewById<EditText>(R.id.et_add_book_genre).text.toString() != bookDetails.genre) {
            bookHashMap[Constants.GENRE] =
                findViewById<EditText>(R.id.et_add_book_genre).text.toString()
        }
        if (findViewById<EditText>(R.id.et_add_book_pages).text.toString() != bookDetails.pages) {
            bookHashMap[Constants.PAGES] =
                findViewById<EditText>(R.id.et_add_book_pages).text.toString()
        }
        if (findViewById<EditText>(R.id.et_add_book_publish_year).text.toString() != bookDetails.publishedYear) {
            bookHashMap[Constants.PUBLISH_YEAR] =
                findViewById<EditText>(R.id.et_add_book_publish_year).text.toString()
        }
        if (findViewById<RatingBar>(R.id.et_add_book_rate).rating != bookDetails.myRate) {
            bookHashMap[Constants.MY_RATE] =
                findViewById<RatingBar>(R.id.et_add_book_rate).rating
        }
        if (findViewById<EditText>(R.id.et_add_book_pages_read).text.toString() != bookDetails.pagesRead) {
            bookHashMap[Constants.PAGES_READ] =
                findViewById<EditText>(R.id.et_add_book_pages_read).text.toString()
        }
        if (findViewById<EditText>(R.id.et_add_start_reading_date).text.toString() != bookDetails.startDate) {
            bookHashMap[Constants.START_DATE] =
                findViewById<EditText>(R.id.et_add_start_reading_date).text.toString()
        }
        if (findViewById<EditText>(R.id.et_add_finish_reading_date).text.toString() != bookDetails.finishDate) {
            bookHashMap[Constants.FINISH_DATE] =
                findViewById<EditText>(R.id.et_add_finish_reading_date).text.toString()
        }
        if (findViewById<EditText>(R.id.et_add_book_notes).text.toString() != bookDetails.notes) {
            bookHashMap[Constants.NOTES] =
                findViewById<EditText>(R.id.et_add_book_notes).text.toString()
        }
        //calls the database function that updates the book
        RealTimeDataBase().updateBookDetails(bookId, this, bookHashMap)
    }


    /**
     * updates the date to be the one selected from the calendar by user
     */
    private fun updateDate(date: EditText) {
        val format = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        //updates the date that the user puts in the start date and finish date
        when (date.id) {
            R.id.et_add_start_reading_date -> {
                findViewById<EditText>(R.id.et_add_start_reading_date).setText(
                    sdf.format(calendar.time).toString()
                )
            }

            R.id.et_add_finish_reading_date -> {
                findViewById<EditText>(R.id.et_add_finish_reading_date).setText(
                    sdf.format(calendar.time).toString()
                )
            }
        }
    }

    /**
     * It is a Custom Dialog that appears on the back button that asks if the user wants to save the modified details about the book
     */
    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_update_confirmation)
        customDialog.findViewById<TextView>(R.id.tv_no).setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.findViewById<TextView>(R.id.tv_yes).setOnClickListener {
            makeProgressDialogVisible(resources.getString(R.string.please_wait))
            updateBookDetails()
            customDialog.dismiss()
        }
        customDialog.show()
    }
}