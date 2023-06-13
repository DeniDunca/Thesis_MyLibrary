package com.example.mylibrary.utils
import android.view.View
import android.widget.RatingBar
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

class SetRatingAction(private val rating: Float) : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isAssignableFrom(RatingBar::class.java)
    }

    override fun getDescription(): String {
        return "Set rating on RatingBar"
    }

    override fun perform(uiController: UiController, view: View) {
        val ratingBar = view as RatingBar
        ratingBar.rating = rating
    }
}