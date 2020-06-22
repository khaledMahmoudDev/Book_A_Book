package com.example.bookabook.model

data class User(
    var id: String = "",
    var email: String = "",
    var userName : String = "",
 //   var profileImage: String = "",
    var userBooks : ArrayList<BooksModel> = ArrayList(),
    var phoneNumber: String = ""
)
