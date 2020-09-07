package com.example.bookabook.model.fcm

data class DataMessage(
    var id: String = "",
   var bookTitle: String = "",
    var bookWriter: String = "",
//    var bookDescription:String = "",
//    var bookCategory: ArrayList<String> =ArrayList(),
   var bookThumbnail: String = ""
//    var bookOwnerId: String = "",
//    var bookFile : String = ""
)