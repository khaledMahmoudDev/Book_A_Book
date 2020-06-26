package com.example.bookabook.utils

import android.content.Intent

object Utils {
    const val PICK_IMAGE_REQUEST = 111

    fun pickImageIntent(): Intent {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        return intent
    }


}