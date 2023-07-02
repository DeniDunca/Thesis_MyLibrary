package com.example.mylibrary.activities


import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mylibrary.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


open class BaseActivity : AppCompatActivity() {

    private var backButtonClicked = false
    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    /**
     * Function that makes the progress dialog visible
     */
    fun makeProgressDialogVisible(text: String) {
        progressDialog = Dialog(this)
        //set the progress dialog
        progressDialog.setContentView(R.layout.progress_dialog)
        //put a custom text on the progress bar
        progressDialog.findViewById<TextView>(R.id.tv_progress_dialog_text).text = text
        //shows the progress bar
        progressDialog.show()
    }

    /**
     * function that makes the progress dialog invisible
     */
    fun makeProgressDialogInvisible() {
        progressDialog.dismiss()
    }

    /**
     * function that gets the current authenticated user
     */
    fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    /**
     * Function that verifies if the back button was pressed twice for exit
     */
    fun backButtonClickedTwice() {
        if (backButtonClicked) {
            super.onBackPressed()
            return
        }
        //if the back button was pressed once displey toaster
        this.backButtonClicked = true
        Toast.makeText(this, getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_LONG)
            .show()

        //the backButtonClicked reset after 2000 ms
        Handler().postDelayed({
            backButtonClicked = false
        }, 2000)
    }

    /**
     * Function for displaying customizable error
     */
    fun displayError(message: String) {
        //creates a snackbar and put the message and the color for it
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.error_color))
        //shows the progress bar
        snackBar.show()
    }
}