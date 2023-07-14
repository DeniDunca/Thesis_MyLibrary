package com.example.mylibrary.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylibrary.R
import com.example.mylibrary.adapters.RecommendedItemsAdapter
import com.example.mylibrary.database.RealTimeDataBase
import com.example.mylibrary.models.Archive
import com.example.mylibrary.models.User
import com.example.mylibrary.utils.Constants
import com.example.mylibrary.utils.SwipeToAddCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import de.hdodenhof.circleimageview.CircleImageView
import org.tensorflow.lite.Interpreter
import java.io.File

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val MY_PROFILE_CODE: Int = 9
    }

    //declare variables
    private lateinit var userId: String
    private var interpreter: Interpreter? = null
    private var prediction: Array<Array<String>> = arrayOf(arrayOf(""))
    private var modelFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //sets up action bar
        setupActionBar()

        //get the recommended books for user from database
        //getTopBooks()
        getRecommendedBooks()

        //sets the navigation menu to the nav view
        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener(this)

        //function to redirect to my profile when the user image is clicked in the nav menu
        clickOnUserImage()

        //gets the user data from fireStore
        RealTimeDataBase().loadUserData(this)

        //when clicked on the plus button adds a book by selecting from the two actions
        findViewById<FloatingActionButton>(R.id.fab_add_book).setOnClickListener {
            val addBookDialog = AlertDialog.Builder(this)
            //set the alert dialog title
            addBookDialog.setTitle("Select Action")
            val addBookDialogItems =
                arrayOf("Search books by keyword or ISBN", "Add new book manually")
            //set the alert dialog option titles
            addBookDialog.setItems(addBookDialogItems) { _, which ->
                when (which) {
                    0 -> {//when clicked on first option go to SearchArchiveActivity
                        val intent = Intent(this, SearchArchiveActivity::class.java)
                        intent.putExtra(Constants.USERID, userId)
                        startActivity(intent)
                    }

                    1 -> {//when clicked on first option go to AddBookActivity
                        val intent = Intent(this, AddBookActivity::class.java)
                        intent.putExtra(Constants.USERID, userId)
                        startActivity(intent)
                    }
                }
            }
            //show the alert dialog
            addBookDialog.show()
        }
    }


    /**
     * When burger icon is pressed the drawer menu toggles
     */
    private fun setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar_main))
        //get toolbar id
        supportActionBar?.title = resources.getString(R.string.rec_books)
        //set the menu button icon, color and action (opens navigation bar)
        findViewById<Toolbar>(R.id.toolbar_main).setNavigationIcon(R.drawable.ic_menu)
        findViewById<Toolbar>(R.id.toolbar_main).setBackgroundColor(resources.getColor(R.color.purple_200))
        findViewById<Toolbar>(R.id.toolbar_main).setNavigationOnClickListener {
            toggleDrawer()
        }

    }

    /**
     * Opens and closes the drawer menu
     */
    private fun toggleDrawer() {
        if (findViewById<DrawerLayout>(R.id.drawer_layout).isDrawerOpen(GravityCompat.START)) {
            findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        } else {
            findViewById<DrawerLayout>(R.id.drawer_layout).openDrawer(GravityCompat.START)
        }
    }

    /**
     * Closes the drawer menu on back button pressed
     */
    override fun onBackPressed() {
        if (findViewById<DrawerLayout>(R.id.drawer_layout).isDrawerOpen(GravityCompat.START)) {
            findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        } else {
            backButtonClickedTwice()
        }
    }

    /**
     * Method to click on user image from navbar header
     */
    private fun clickOnUserImage() {
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)
        val userImage: CircleImageView = headerView.findViewById(R.id.nav_user_image)
        userImage.setOnClickListener {
            startActivityForResult(Intent(this, MyProfileActivity::class.java), MY_PROFILE_CODE)
        }
    }

    /**
     * Adds navigation to the drawer menu buttons
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_my_books -> {//goes to MyBooksActivity
                startActivity(Intent(this, MyBooksActivity::class.java))
            }

            R.id.nav_my_stats -> {//goes to StatisticsActivity
                startActivity(Intent(this, StatisticsActivity::class.java))
            }

            R.id.nav_my_profile -> {//goes to MyProfileActivity
                startActivityForResult(Intent(this, MyProfileActivity::class.java), MY_PROFILE_CODE)
            }

            R.id.nav_sign_out -> {//disconnects the user
                modelFile = null
                interpreter = null
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        return true
    }


    /**
     * Set image and name of the user from database
     */
    fun updateNavigationUserDetails(user: User) {
        userId = user.id

        //put the user image from database into the element for user image in the menu
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user)
            .into(findViewById<CircleImageView>(R.id.nav_user_image))

        //sets the firstname and the lastname of the user on the menu
        findViewById<TextView>(R.id.tv_firstname_lastname).text =
            user.firstname + " " + user.lastname
    }

    /**
     * Checks if there are any changes to the data and load the updated user data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //send the data about user to the MyProfileActivity
        if (resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_CODE) {
            RealTimeDataBase().loadUserData(this)
        } //send the data about user's books to the MyBooksActivity
        else if (resultCode == Activity.RESULT_OK && requestCode == MyBooksActivity.MY_BOOK_CODE) {
            getTopBooks()
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    /**
     * populates the archive page with the books from the realtime database
     */
    fun populatesRecommendedList(bookList: List<Archive>) {
        val rvBookList = findViewById<RecyclerView>(R.id.rv_rec_list)
        //if there are recommended books for the user
        if (bookList.size > 0) {
            //make the list of books visible
            rvBookList.visibility = View.VISIBLE
            //make the default message not visible
            findViewById<TextView>(R.id.tv_no_rec).visibility = View.GONE

            //put the layout to the recyclerView
            rvBookList.layoutManager = LinearLayoutManager(this)
            findViewById<RecyclerView>(R.id.rv_rec_list).setHasFixedSize(true)

            //put the adapter to the recyclerView
            val adapter = RecommendedItemsAdapter(this, bookList)
            rvBookList.adapter = adapter

            //put the action for when an element is swiped to the right
            val addSwipeHandler = object : SwipeToAddCallback(this) {
                // on swiped the book is added to the user's collection
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    adapter.notifyAddItem(this@MainActivity, viewHolder.adapterPosition)

                }
            }

            //touch helper for books
            val addItemTouchHelper = ItemTouchHelper(addSwipeHandler)
            addItemTouchHelper.attachToRecyclerView(findViewById(R.id.rv_rec_list))

            //on click on preview of the book show info about book
            adapter.setOnClickListener(object :
                RecommendedItemsAdapter.OnClickListener {
                override fun onClick(position: Int, model: Archive) {
                    val intent = Intent(this@MainActivity, ArchiveInfoActivity::class.java)
                    intent.putExtra(Constants.ISBN, model.isbn)
                    startActivity(intent)
                }
            })
        } else {
            rvBookList.visibility = View.GONE
            findViewById<TextView>(R.id.tv_no_rec).visibility = View.VISIBLE
        }
    }

    /**
     * gets the top 10 books from database
     */
    private fun getTopBooks() {
        RealTimeDataBase().getFirstBestBooks(this)
    }

    private fun getPredictionsFromFirebaseMLModel(
        userId: String,
        callback: (Array<Array<String>>) -> Unit
    ) {
        if (modelFile != null && interpreter != null) {
            prediction = predict(interpreter!!, userId)
            callback(prediction)
        } else {
            //download the model from Firebase Machine Learning
            val conditions = CustomModelDownloadConditions.Builder().build()
            FirebaseModelDownloader.getInstance()
                .getModel("final_model", DownloadType.LATEST_MODEL, conditions)
                .addOnSuccessListener { model: CustomModel? ->
                    modelFile = model?.file
                    //if the model is downloaded correctly git it to an interpreter
                    if (modelFile != null) {
                        interpreter = Interpreter(modelFile!!)
                        //give the interpreter to the prediction method
                        prediction = predict(interpreter!!, userId)
                        //return the result
                        callback(prediction)
                    }
                }
                .addOnFailureListener {
                    //in case of failure return a message
                    prediction = arrayOf(arrayOf("Sorry! The model could not be downloaded!"))
                    callback(prediction)
                }
        }
    }

    /**
     * Prediction method that senst in the inputs to the model and returns the outputs
     */
    private fun predict(interpreter: Interpreter, userId: String): Array<Array<String>> {
        //the input is the user id
        val inputs = arrayOf(userId.toByteArray())
        //the outputs, we only need output2, it contains the isbn list
        val output1 = Array(1) { FloatArray(20) }
        val output2 = Array(1) { Array(20) { "" } }
        val outputs = mutableMapOf<Int, Any>(0 to output1, 1 to output2)
        //run the model with the inputs and put the results in the outputs
        interpreter.runForMultipleInputsOutputs(inputs, outputs)

        //return the isbn list
        return output2
    }

    /**
     * Gets the isbns returned by the prediction and sends it to the database call to return the book details
     */
    private fun getRecommendedBooks() {
        getPredictionsFromFirebaseMLModel(getCurrentUserId()) { predictions ->
            val flattenedPredictions = predictions.flatten()
            RealTimeDataBase().getRecommendedBooks(this, flattenedPredictions)
        }
    }

}
