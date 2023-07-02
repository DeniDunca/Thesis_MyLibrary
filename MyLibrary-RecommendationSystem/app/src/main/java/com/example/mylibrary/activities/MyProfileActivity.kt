package com.example.mylibrary.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
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
import com.example.mylibrary.models.User
import com.example.mylibrary.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class MyProfileActivity : BaseActivity() {

    private var selectedImageUri: Uri? = null
    private var profileImageUrl: String = ""
    private lateinit var userDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        //hide the top bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //set up action bar
        setupActionBar()

        //get user data
        RealTimeDataBase().loadUserData(this)

        //checking permissions, if it has rights than open image chooser
        findViewById<ImageView>(R.id.iv_my_profile_image).setOnClickListener {

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
        findViewById<Button>(R.id.btn_my_profile_update).setOnClickListener {
            if (selectedImageUri != null) {
                uploadImage()
            } else {
                makeProgressDialogVisible(resources.getString(R.string.please_wait))
                updateUserProfile()
            }
        }
    }

    /**
     * Sets up the action bar for the page with back button
     */
    private fun setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar_my_profile))
        //get toolbar id
        findViewById<Toolbar>(R.id.toolbar_my_profile).setBackgroundColor(resources.getColor(R.color.purple_200))
        val actionBar = supportActionBar
        //change the title and add back button with icon
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_back)
            actionBar.title = resources.getString(R.string.my_profile)
        }
        //set the back button
        findViewById<Toolbar>(R.id.toolbar_my_profile).setNavigationOnClickListener {
            onBackPressed()
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
        //if the user has permission, then he can add user profile picture
        if (requestCode == Constants.READ_STORAGE_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                //else shows a message to go change the permission settings
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
        //get the data about the user and populate teh image
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_CODE && data!!.data != null) {
            selectedImageUri = data.data
            try {
                Glide
                    .with(this@MyProfileActivity)
                    .load(selectedImageUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .into(findViewById(R.id.iv_my_profile_image))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Gets data from data base and put it on the page
     */
    fun setUserData(user: User) {
        userDetails = user
        //gets the user image from database and puts it in the use image element with Glide
        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user)
            .into(findViewById(R.id.iv_my_profile_image))
        //gets the user details from database and puts in the elements on the interface
        findViewById<EditText>(R.id.et_my_profile_firstname).setText(user.firstname)
        findViewById<EditText>(R.id.et_my_profile_lastname).setText(user.lastname)
        findViewById<EditText>(R.id.et_my_profile_email).setText(user.email)
        if (user.mobile != "") {
            findViewById<EditText>(R.id.et_my_profile_mobile).setText(user.mobile)

        }
    }

    /**
     * upload to storage the images
     */
    private fun uploadImage() {
        makeProgressDialogVisible(resources.getString(R.string.please_wait))
        //if the user uploads a new image, stores it in the Storage database in Firebase
        if (selectedImageUri != null) {
            val ref: StorageReference = FirebaseStorage.getInstance().reference.child(
                "User_image" + System.currentTimeMillis() + "." + Constants.getFileExtension(
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
                    profileImageUrl = uri.toString()
                    updateUserProfile()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this@MyProfileActivity, e.message, Toast.LENGTH_LONG).show()
                makeProgressDialogInvisible()
            }
        }
    }


    /**
     * Update the profile from fireStore with the image from storage
     */
    fun profileUpdateSuccess() {
        makeProgressDialogInvisible()
        setResult(Activity.RESULT_OK)
        finish()
    }

    /**
     * Verifies the modifications from the my profile activity and calls the firebase function to change them in the database
     */
    private fun updateUserProfile() {
        val userHashMap = HashMap<String, Any>()

        //if the image is empty or is the same as the one in database we do no changes
        if (profileImageUrl.isNotEmpty() && profileImageUrl != userDetails.image) {
            userHashMap[Constants.IMAGE] = profileImageUrl
        }
        //update the rest of elements
        if (findViewById<EditText>(R.id.et_my_profile_firstname).text.toString() != userDetails.firstname) {
            userHashMap[Constants.FIRSTNAME] =
                findViewById<EditText>(R.id.et_my_profile_firstname).text.toString()
        }
        if (findViewById<EditText>(R.id.et_my_profile_lastname).text.toString() != userDetails.lastname) {
            userHashMap[Constants.LASTNAME] =
                findViewById<EditText>(R.id.et_my_profile_lastname).text.toString()
        }
        if (findViewById<EditText>(R.id.et_my_profile_mobile).text.toString() != userDetails.mobile) {
            userHashMap[Constants.MOBILE] =
                findViewById<EditText>(R.id.et_my_profile_mobile).text.toString()
        }

        //calls the method that updates the user details in the database
        RealTimeDataBase().updateUserProfile(this, userHashMap)
    }

}