package com.example.bookabook.utils

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingServices : FirebaseMessagingService() {
    private val TAG = "MyFirebaseMessagingServ"

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        var notificatiobBody = ""
        var notificationTitle = ""
        var notificationData = ""
        try {
            notificationData = p0.data.toString()

            val text = p0.data["bookTitle"]

            val notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager

            if (text != null) {
                notificationManager.sendBookNotification(text,applicationContext)
                Log.d("notiffffica", "outside function")
            }else
            {
                Log.d(TAG, "onMessageReceived: null pointer sendnotification")
            }

        } catch (
            e: NullPointerException
        ) {
            Log.d(TAG, "onMessageReceived: null pointer ex ${e.message}")
        }


        Log.d(TAG, "onMessageReceived: data $notificationData")
        Log.d(TAG, "onMessageReceived: not body $notificatiobBody ")
        Log.d(TAG, "onMessageReceived: title $notificationTitle")

    }

}