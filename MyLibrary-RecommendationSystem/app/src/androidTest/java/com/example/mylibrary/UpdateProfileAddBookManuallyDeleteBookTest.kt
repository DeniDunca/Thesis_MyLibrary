package com.example.mylibrary

import SetSearchViewTextAction
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mylibrary.dataclass.AccountData
import com.example.mylibrary.dataclass.ManuallyAddedBookData
import com.example.mylibrary.utils.SwipeRightFromLeftEdge
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UpdateProfileAddBookManuallyDeleteBookTest {

    @Test
    fun createAccountAddBooksToLibraryAndRateEachBook() {

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val intent = appContext.packageManager.getLaunchIntentForPackage(appContext.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        appContext.startActivity(intent)

        Thread.sleep(6000)

        val accountData = AccountData("test","test","test@gmail.com", "123456")
        val bookData = ManuallyAddedBookData("testBook","testAuthor","testDescription", "1234567", "2023", "testGenre", "123")
        login(accountData)
        Thread.sleep(6000)

        updateProfile()
        Thread.sleep(6000)

        addBookManually(bookData)
        Thread.sleep(6000)

        searchForBookAndDeleteIt(bookData)

    }

    private fun login(accountData: AccountData) {
        Espresso.onView(ViewMatchers.withId(R.id.btn_sign_in))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_sign_in))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.et_email_sign_in))
            .perform(ViewActions.typeText(accountData.email))
        Espresso.onView(ViewMatchers.withId(R.id.et_password_sign_in))
            .perform(ViewActions.typeText(accountData.password))
            .perform(ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_signIn))
            .perform(ViewActions.click())
    }

    private fun updateProfile() {
        Espresso.onView(ViewMatchers.isRoot()).perform(SwipeRightFromLeftEdge())
        Thread.sleep(6000)
        Espresso.onView(ViewMatchers.withId(R.id.nav_my_profile))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.nav_my_profile))
            .perform(ViewActions.click())
        Thread.sleep(6000)
        Espresso.onView(ViewMatchers.withId(R.id.et_my_profile_firstname))
            .perform(ViewActions.typeText("test"))
        Espresso.onView(ViewMatchers.withId(R.id.et_my_profile_lastname))
            .perform(ViewActions.typeText("test"))
            .perform(ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_my_profile_update))
            .perform(ViewActions.click())
        Thread.sleep(6000)
        Espresso.onView(ViewMatchers.isRoot()).perform(SwipeRightFromLeftEdge())
        Espresso.onView(ViewMatchers.withId(R.id.tv_firstname_lastname))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tv_firstname_lastname))
            .check(ViewAssertions.matches(withText("testtest" + " " + "testtest")))

    }

    private fun addBookManually(manualBook: ManuallyAddedBookData){
        Espresso.onView(ViewMatchers.withId(R.id.fab_add_book))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fab_add_book))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.fab_add_book))
            .perform(ViewActions.click())
        Espresso.onView(withText("Select Action"))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withText("Add new book manually"))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click())
        Thread.sleep(6000)
        Espresso.onView(ViewMatchers.withId(R.id.et_add_book_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.et_add_book_title))
            .perform(ViewActions.typeText(manualBook.title))
        Espresso.onView(ViewMatchers.withId(R.id.et_add_book_author))
            .perform(ViewActions.typeText(manualBook.author))
        Espresso.onView(ViewMatchers.withId(R.id.et_add_book_description))
            .perform(ViewActions.scrollTo(),ViewActions.typeText(manualBook.description))
            .perform(ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.et_add_book_isbn))
            .perform(ViewActions.scrollTo(),ViewActions.typeText(manualBook.isbn))
            .perform(ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.et_add_book_genre))
            .perform(ViewActions.scrollTo(),ViewActions.typeText(manualBook.genre))
            .perform(ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.et_add_book_pages))
            .perform(ViewActions.scrollTo(),ViewActions.typeText(manualBook.pages))
            .perform(ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.et_add_book_publish_year))
            .perform(ViewActions.scrollTo(),ViewActions.typeText(manualBook.publishedYear))
            .perform(ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_add_book_to_collection))
            .perform(ViewActions.scrollTo(),ViewActions.click())
    }

    private fun searchForBookAndDeleteIt(manualBook: ManuallyAddedBookData){
        Espresso.onView(ViewMatchers.isRoot()).perform(SwipeRightFromLeftEdge())
        Espresso.onView(ViewMatchers.withId(R.id.nav_my_books))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.nav_my_books))
            .perform(ViewActions.click())
        Thread.sleep(6000)
        Espresso.onView(ViewMatchers.withId(R.id.search_my_books))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.search_my_books))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.search_my_books))
            .perform(SetSearchViewTextAction(manualBook.isbn))
            .perform(ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.rv_books_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.swipeLeft()))

    }
}