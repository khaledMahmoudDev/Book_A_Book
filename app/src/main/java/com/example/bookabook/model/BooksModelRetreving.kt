package com.example.bookabook.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BooksModelRetreving(
    var id: String = "",
    var bookTitle: String = "",
    var bookWriter: String = "",
    var bookDescription: String = "",
    var bookCategory: ArrayList<String> = ArrayList(),
    var bookAvailability: Boolean = true,
    var bookAddedDate: Long = 0L,
    var bookAddedDateString: String = "",
    var bookThumbnail: String = "",
    var bookOwnerId: String = "",
    var isNew: Boolean = true
) : Parcelable