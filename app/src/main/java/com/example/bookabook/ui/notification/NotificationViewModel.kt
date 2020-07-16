package com.example.bookabook.ui.notification

import android.app.Application
import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.example.bookabook.utils.sendBookNotification

class NotificationViewModel(val app: Application) : AndroidViewModel(app) {

    init {
        sendNow()

    }


    fun sendNow() {
        Log.d("click", "Clicked")
        val notificationManager = ContextCompat.getSystemService(
            app,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendBookNotification("hellow world 5ara", app)

    }
}