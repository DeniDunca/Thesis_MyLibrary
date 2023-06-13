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
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mylibrary.dataclass.AccountData
import com.example.mylibrary.dataclass.BooksData
import com.example.mylibrary.utils.ClearSearchViewAction
import com.example.mylibrary.utils.SetRatingAction
import com.example.mylibrary.utils.SwipeRightFromLeftEdge
import com.example.mylibrary.utils.TestUtils

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private val utils = TestUtils()

    @Test
    fun createAccountAddBooksToLibraryAndRateEachBook() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val intent = appContext.packageManager.getLaunchIntentForPackage(appContext.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        appContext.startActivity(intent)

        Thread.sleep(6000)

        val accountDataList = utils.readAccountDataFromFile("account_data.txt")
        val bookDataList = utils.readBooksDataFromFile("book_data.txt")


        val booksPerUser = 10 // Number of books to assign per user
        val totalUsers = accountDataList.size
        val totalBooks = bookDataList.size

        for (i in 0 until totalUsers) {
            val accountData = accountDataList[i]
            createAccount(accountData)
            Thread.sleep(6000)
            login(accountData)
            Thread.sleep(8000)

            val startIndex = i * booksPerUser
            val endIndex = minOf(startIndex + booksPerUser, totalBooks)
            val userBooksDataList = bookDataList.subList(startIndex, endIndex)

            clickOnFab()

            for (bookData in userBooksDataList) {
                searchForBooksAndAddToLibrary(bookData)
            }

            navToBooks()

            for (bookData in userBooksDataList) {
                addRatingForEachBook(bookData)
            }

            logout()
        }

    }

    private fun createAccount(accountData: AccountData) {
        Espresso.onView(ViewMatchers.withId(R.id.btn_sign_up))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_sign_up))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.et_firstname_sign_up))
            .perform(ViewActions.typeText(accountData.firstname))
        Espresso.onView(ViewMatchers.withId(R.id.et_lastname_sign_up))
            .perform(ViewActions.typeText(accountData.lastname))
        Espresso.onView(ViewMatchers.withId(R.id.et_email_sign_up))
            .perform(ViewActions.typeText(accountData.email))
        Espresso.onView(ViewMatchers.withId(R.id.et_password_sign_up))
            .perform(ViewActions.typeText(accountData.password))
            .perform(ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.et_confirm_password_sign_up))
            .perform(ViewActions.typeText(accountData.password))
            .perform(ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.btn_signUp))
            .perform(ViewActions.click())
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

    private fun clickOnFab(){
        Espresso.onView(ViewMatchers.withId(R.id.fab_add_book))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fab_add_book))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("Select Action"))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText("Search books by keyword or ISBN"))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click())
    }
    private fun searchForBooksAndAddToLibrary(booksData: BooksData){
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.search_archive))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.search_archive))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.search_archive))
            .perform(SetSearchViewTextAction(booksData.isbn))
            .perform(ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.rv_archive_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.swipeRight()))
        Espresso.onView(ViewMatchers.withId(R.id.search_archive))
            .perform(ClearSearchViewAction())

    }

    private fun navToBooks(){
        Espresso.pressBack()
        Espresso.pressBack()
        Espresso.onView(ViewMatchers.isRoot()).perform(SwipeRightFromLeftEdge())
        Espresso.onView(ViewMatchers.withId(R.id.nav_my_books))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.nav_my_books))
            .perform(ViewActions.click())
        Thread.sleep(6000)
    }

    private fun addRatingForEachBook(booksData: BooksData){
        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(R.id.search_my_books))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.search_my_books))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.search_my_books))
            .perform(SetSearchViewTextAction(booksData.isbn))
            .perform(ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.rv_books_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.swipeRight()))
        val rating = booksData.rating.toFloat()
        Espresso.onView(ViewMatchers.withId(R.id.et_add_book_rate))
            .perform(SetRatingAction(rating))

        Espresso.onView(ViewMatchers.withId(R.id.btn_update_book))
            .perform(ViewActions.scrollTo())
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.search_my_books))
            .perform(ClearSearchViewAction())

    }

    private fun logout(){
        Espresso.pressBack()
        Espresso.pressBack()
        Espresso.onView(ViewMatchers.isRoot()).perform(SwipeRightFromLeftEdge())
        Espresso.onView(ViewMatchers.withId(R.id.nav_sign_out))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.nav_sign_out))
            .perform(ViewActions.click())
        Thread.sleep(6000)
    }
}