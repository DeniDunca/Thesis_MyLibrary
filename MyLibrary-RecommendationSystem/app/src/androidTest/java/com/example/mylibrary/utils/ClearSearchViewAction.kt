package com.example.mylibrary.utils

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

class ClearSearchViewAction : ViewAction {
    override fun getDescription(): String {
        return "Clear view text"
    }

    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isAssignableFrom(SearchView::class.java)
    }

    override fun perform(uiController: UiController?, view: View?) {
        val searchView = view as SearchView
        searchView.setQuery("", false)
        searchView.isIconified = true
    }
}
