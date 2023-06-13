package com.example.mylibrary.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mylibrary.R
import com.example.mylibrary.database.RealTimeDataBase

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //hiding the top bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //adding the font of the title
        val typeFace: Typeface = Typeface.createFromAsset(assets, "Port Credit.otf")
        val tvAppName = findViewById<TextView>(R.id.tv_my_library)
        tvAppName.typeface = typeFace

        //delay before opening the intro page or main page depending if the user was logged in before
        Handler().postDelayed({
            var currentUserId = RealTimeDataBase().getCurrentUserID()

            if (currentUserId.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()
        }, 2500)
    }


}