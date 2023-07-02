package com.example.mylibrary.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mylibrary.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        //adding the text font to the title on the page
        val typeFace: Typeface = Typeface.createFromAsset(assets, "Port Credit.otf")
        val tvAppName = findViewById<TextView>(R.id.tv_my_library_login)
        tvAppName.typeface = typeFace

        //hide the top bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //creates the buttons
        val btnSignUp = findViewById<Button>(R.id.btn_sign_up)
        val btnSignIn = findViewById<Button>(R.id.btn_sign_in)

        //if sign up button was clicked go to sigh up page
        btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        //if sign in button was clicked go to sigh in page
        btnSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}