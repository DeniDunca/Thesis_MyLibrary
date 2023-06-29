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

    private lateinit var userId: String
    private var interpreter : Interpreter? = null
    private var prediction :  Array<Array<String>> = arrayOf(arrayOf(""))
    private var modelFile : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //sets up action bar
        setupActionBar()

        //get the bestsellers from database
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
            addBookDialog.setTitle("Select Action")
            val addBookDialogItems =
                arrayOf("Search books by keyword or ISBN", "Add new book manually")
            addBookDialog.setItems(addBookDialogItems) { _, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(this, SearchArchiveActivity::class.java)
                        intent.putExtra(Constants.USERID, userId)
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(this, AddBookActivity::class.java)
                        intent.putExtra(Constants.USERID, userId)
                        startActivity(intent)
                    }
                }
            }
            addBookDialog.show()

        }

    }


    /**
     * When burger icon is pressed the drawer menu toggles
     */
    private fun setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar_main))
        supportActionBar?.title = resources.getString(R.string.rec_books)
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
            R.id.nav_my_books -> {
                startActivity(Intent(this, MyBooksActivity::class.java))
            }
            R.id.nav_my_stats -> {
                startActivity(Intent(this, StatisticsActivity::class.java))
            }
            R.id.nav_my_profile -> {
                startActivityForResult(Intent(this, MyProfileActivity::class.java), MY_PROFILE_CODE)
            }
            R.id.nav_sign_out -> {
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

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user)
            .into(findViewById<CircleImageView>(R.id.nav_user_image))

        findViewById<TextView>(R.id.tv_firstname_lastname).text =
            user.firstname + " " + user.lastname
    }

    /**
     * Checks if there are any changes to the data and load the updated user data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_CODE) {
            RealTimeDataBase().loadUserData(this)
        } else if (resultCode == Activity.RESULT_OK && requestCode == MyBooksActivity.MY_BOOK_CODE) {
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
        if (bookList.size > 0) {
            rvBookList.visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_no_rec).visibility = View.GONE

            rvBookList.layoutManager = LinearLayoutManager(this)
            findViewById<RecyclerView>(R.id.rv_rec_list).setHasFixedSize(true)

            val adapter = RecommendedItemsAdapter(this, bookList)
            rvBookList.adapter = adapter


            val addSwipeHandler = object : SwipeToAddCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    adapter.notifyAddItem(this@MainActivity, viewHolder.adapterPosition)

                }
            }

            val addItemTouchHelper = ItemTouchHelper(addSwipeHandler)
            addItemTouchHelper.attachToRecyclerView(findViewById(R.id.rv_rec_list))


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

    private fun getPredictionsFromFirebaseMLModel(userId: String, callback: (Array<Array<String>>) -> Unit) {
        if (modelFile != null && interpreter != null) {
            prediction = predict(interpreter!!, userId)
            callback(prediction)
        } else {
            val conditions = CustomModelDownloadConditions.Builder().requireWifi().build()
            FirebaseModelDownloader.getInstance()
                .getModel("final_model", DownloadType.LATEST_MODEL,conditions)
                .addOnSuccessListener { model: CustomModel? ->
                    modelFile = model?.file
                    if (modelFile != null) {
                        interpreter = Interpreter(modelFile!!)
                        prediction = predict(interpreter!!, userId)
                        callback(prediction)
                    }
                }
                .addOnFailureListener {
                    prediction = arrayOf(arrayOf("Sorry! The model could not be downloaded!"))
                    callback(prediction)
                }
        }
    }

    private fun predict(interpreter: Interpreter, userId: String): Array<Array<String>> {
        val inputs = arrayOf(userId.toByteArray())
        val output1 = Array(1) { FloatArray(20) }
        val output2 = Array(1) { Array(20) { "" } }
        val outputs = mutableMapOf<Int, Any>(0 to output1, 1 to output2)
        interpreter.runForMultipleInputsOutputs(inputs, outputs)

        return output2
    }

    private fun getRecommendedBooks() {
        getPredictionsFromFirebaseMLModel(getCurrentUserId()) { predictions ->
            val flattenedPredictions = predictions.flatten()
            RealTimeDataBase().getRecommendedBooks(this, flattenedPredictions)
        }
    }



}
