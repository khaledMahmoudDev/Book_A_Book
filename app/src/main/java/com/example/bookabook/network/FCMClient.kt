package com.example.bookabook.network

import com.example.bookabook.utils.FCM_BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()


val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(FCM_BASE_URL)
    .build()


object FCMClient {
    val fcmMessage: FCMApi by lazy {
        retrofit.create(FCMApi::class.java)
    }
}


