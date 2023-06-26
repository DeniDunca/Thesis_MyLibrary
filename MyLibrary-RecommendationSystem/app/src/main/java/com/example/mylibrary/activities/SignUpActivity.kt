package com.example.mylibrary.activities

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
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //hiding the top bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //adding the back icon to the action bar
        setUpActionBar()
    }

    /**
     * Function that adds the back button to the action bar on the page
     */
    private fun setUpActionBar() {
        val toolbarSignup = findViewById<Toolbar>(R.id.toolbar_sign_up)
        setSupportActionBar(toolbarSignup)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_back)

        }

        toolbarSignup.setNavigationOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btn_signUp).setOnClickListener {
            signUpUser()
        }
    }

    /**
     * function that validates the fields from the form and register the user
     */
    private fun signUpUser() {
        //the fields from the form trimmed
        val firstname: String =
            findViewById<EditText>(R.id.et_firstname_sign_up).text.toString().trim { it <= ' ' }
        val lastname: String =
            findViewById<EditText>(R.id.et_lastname_sign_up).text.toString().trim { it <= ' ' }
        val email: String =
            findViewById<EditText>(R.id.et_email_sign_up).text.toString().trim { it <= ' ' }
        val password: String =
            findViewById<EditText>(R.id.et_password_sign_up).text.toString().trim { it <= ' ' }
        val confirmPassword: String =
            findViewById<EditText>(R.id.et_confirm_password_sign_up).text.toString()
                .trim { it <= ' ' }

        if (validateForm(firstname, lastname, email, password, confirmPassword)) {
            //show progress bar
            makeProgressDialogVisible(resources.getString(R.string.please_wait))
            //create an user with given pass and email
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, firstname, lastname, registeredEmail)
                        RealTimeDataBase().createUser(this, user)
                    } else {
                        Toast.makeText(this, "Registration failed! The email already exists", Toast.LENGTH_LONG).show()
                        makeProgressDialogInvisible()
                    }
                }
        }
    }

    /**
     * shows message for registered
     */
    fun userRegisteredSuccess() {
        Toast.makeText(this, "You have registered!", Toast.LENGTH_LONG).show()
        makeProgressDialogInvisible()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    /**
     * Function that verifies if the fields of the sign up form are not empty
     */
    private fun validateForm(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return when {
            TextUtils.isEmpty(firstname) -> {
                displayError("Please enter your firstname")
                false
            }
            TextUtils.isEmpty(lastname) -> {
                displayError("Please enter your lastname")
                false
            }
            TextUtils.isEmpty(email) -> {
                displayError("Please enter an email")
                false
            }
            !email.contains("@") -> {
                displayError("Please enter a valid email")
                false
            }

            TextUtils.isEmpty(password) -> {
                displayError("Please enter a password")
                false
            }
            password.length < 6 -> {
                displayError("Password should have at least 6 characters")
                false
            }
            TextUtils.isEmpty(confirmPassword) -> {
                displayError("Please confirm the password")
                false
            }
            !TextUtils.equals(password, confirmPassword) -> {
                displayError("Please confirm the password")
                false
            }
            else -> {
                true
            }
        }
    }
}