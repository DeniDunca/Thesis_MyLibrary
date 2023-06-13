package com.example.mylibrary.models

import android.os.Parcel
import android.os.Parcelable

data class Archive(
    var documentId: String = "",
    val title: String = "",
    val author: String = "",
    val rating: String = "",
    val description: String = "",
    val isbn: String = "",
    val genre: String = "",
    val pages: String = "",
    val publishedYear: String = "",
    val image: String = "",
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
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(documentId)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(rating)
        parcel.writeString(description)
        parcel.writeString(isbn)
        parcel.writeString(genre)
        parcel.writeString(pages)
        parcel.writeString(publishedYear)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Archive> {
        override fun createFromParcel(parcel: Parcel): Archive {
            return Archive(parcel)
        }

        override fun newArray(size: Int): Array<Archive?> {
            return arrayOfNulls(size)
        }
    }
}