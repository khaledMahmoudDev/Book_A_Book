package com.example.bookabook.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.bookabook.R
import com.example.bookabook.ui.MainActivity

// Notification ID.

private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendBookNotification(message: String, applicationContext: Context) {


    val TAG = "notiffffica"
    Log.d(TAG, "inside function")
    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    val contentPendingIntetn = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val bookImage = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.share_free_book)
    val bigImageStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(bookImage)
        .bigLargeIcon(null)


    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.newBookChannelId))
        .setSmallIcon(R.drawable.share_free_book)
        .setContentTitle("New Book Added")
        .setContentText(message)
        .setContentIntent(contentPendingIntetn)
        .setAutoCancel(true)
        .setStyle(bigImageStyle)
        .setLargeIcon(bookImage)


    notify(NOTIFICATION_ID, builder.build())
}