package com.example.bookabook.network

import com.example.bookabook.model.fcm.FireBaseCloudMessage
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface FCMApi {

    @POST("send")
    fun sendNewPostNotification(
        @HeaderMap headers: Map<String, String>,
        @Body message: FireBaseCloudMessage
    ): Call<ResponseBody>
}