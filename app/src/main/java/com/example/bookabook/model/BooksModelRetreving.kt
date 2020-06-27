package com.example.bookabook.model

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
)