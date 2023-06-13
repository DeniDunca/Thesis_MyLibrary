package com.example.mylibrary.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.mylibrary.R
import com.example.mylibrary.database.RealTimeDataBase
import com.example.mylibrary.models.User
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        //hiding the top bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //sign in the user on clicking the button
        findViewById<Button>(R.id.btn_signIn).setOnClickListener {
            signInUser()
        }

        //adding the back icon to the action bar
        setUpActionBar()

    }

    /**
     * Function that adds the back button to the action bar on the page
     */
    private fun setUpActionBar() {
        val toolbarSignIn = findViewById<Toolbar>(R.id.toolbar_sign_in)
        setSupportActionBar(toolbarSignIn)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_back)

        }

        toolbarSignIn.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    /**
     * function that verifies the form fields and authenticate the user
     */
    private fun signInUser() {
        //the fields form the form
        val email: String =
            findViewById<EditText>(R.id.et_email_sign_in).text.toString().trim { it <= ' ' }
        val password: String =
            findViewById<EditText>(R.id.et_password_sign_in).text.toString().trim { it <= ' ' }

        //if validation is true
        if (validateForm(email, password)) {
            //show progress dialog
            makeProgressDialogVisible(resources.getString(R.string.please_wait))
            //authenticate
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //go to main activity
                    RealTimeDataBase().loadUserData(this@SignInActivity)
                } else {
                    Toast.makeText(this, "Signing in failure!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * function that sign in the user
     */
    fun signInSuccess(user: User) {
        makeProgressDialogInvisible()
        startActivity(Intent(this, MainActivity::class.java))
        Toast.makeText(this, "You successfully signed in!", Toast.LENGTH_LONG).show()
        finish()
    }

    /**
     * Function that verifies if the fields of the sign in form are not empty
     */
    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                displayError("Please enter your email")
                false
            }
            TextUtils.isEmpty(password) -> {
                displayError("Please enter your password")
                false
            }
            else -> {
                true
            }
        }
    }
}