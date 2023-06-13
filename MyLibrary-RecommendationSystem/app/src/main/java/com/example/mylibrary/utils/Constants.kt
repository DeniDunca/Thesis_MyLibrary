package com.example.mylibrary.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS: String = "Users"
    const val BOOKS: String = "Books"
    const val ARCHIVE: String = "archive"
    const val USER_BOOK: String = "User_Book"

    const val IMAGE: String = "image"
    const val FIRSTNAME: String = "firstname"
    const val LASTNAME: String = "lastname"
    const val MOBILE: String = "mobile"
    const val READ_STORAGE_CODE = 1
    const val PICK_IMAGE_CODE = 2

    const val USERID: String = "userId"
    const val BOOKID: String = "bookId"
    const val DOCUMENT_ID: String = "documentId"

    const val TITLE: String = "title"
    const val AUTHOR: String = "author"
    const val MY_RATE: String = "myRate"
    const val PAGES_READ: String = "pagesRead"
    const val START_DATE: String = "startDate"
    const val FINISH_DATE: String = "finishDate"
    const val NOTES: String = "notes"
    const val DESCRIPTION: String = "description"
    const val ISBN: String = "isbn"
    const val GENRE: String = "genre"
    const val PAGES: String = "pages"
    const val PUBLISH_YEAR: String = "publishYear"
    const val RATING: String = "rating"

    /**
     * Opens image chooser when the image on my profile is clicked
     */
    fun showImageChooser(activity: Activity) {
        var galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_CODE)
    }

    /**
     * Get the file extension based on the URI
     */
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}