package com.example.bookabook.utils

import android.util.Log
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
            notificationTitle = p0.notification!!.title.toString()
            notificatiobBody = p0.notification!!.body.toString()

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