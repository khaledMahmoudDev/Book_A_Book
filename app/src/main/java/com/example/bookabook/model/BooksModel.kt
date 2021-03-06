package com.example.bookabook.model

data class BooksModel(
    var id: String = "",
    var bookTitle: String = "",
    var bookWriter: String = "",
    var bookDescription:String = "",
    var bookCategory: ArrayList<String> =ArrayList(),
    var bookAddedDate: MutableMap<String, String> = mutableMapOf(),
    var bookThumbnail: String = "",
    var bookOwnerId: String = "",
    var bookFile : String = ""
)