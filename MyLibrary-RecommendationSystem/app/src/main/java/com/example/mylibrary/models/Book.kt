package com.example.mylibrary.models

import android.os.Parcel
import android.os.Parcelable

data class Book(
    val image: String = "",
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val isbn: String = "",
    val genre: String = "",
    val pages: String = "",
    val publishedYear: String = "",
    val rating: String = "",

    var documentId: String = "",
    val myRate: Float = 0.0F,
    val pagesRead: String = "",
    val startDate: String = "",
    val finishDate: String = "",
    val notes: String = "",

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(description)
        parcel.writeString(isbn)
        parcel.writeString(genre)
        parcel.writeString(pages)
        parcel.writeString(publishedYear)
        parcel.writeString(rating)
        parcel.writeString(documentId)
        parcel.writeFloat(myRate)
        parcel.writeString(pagesRead)
        parcel.writeString(startDate)
        parcel.writeString(finishDate)
        parcel.writeString(notes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}