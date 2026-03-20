/*
package com.example.chatmessenger.notifications.network

import com.example.myapplication.notification.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(NotificationAPI::class.java)
        }
    }
}*/
package com.app.hihlo.network_call

import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PushNotificationRetrofitBuilder {

    companion object {
        private val NOTIFICATION_BASE_URL = "https://fcm.googleapis.com/v1/"
        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs full request/response body
        }

        private val client = OkHttpClient.Builder().protocols(listOf(Protocol.HTTP_1_1)).build()
           /* .addInterceptor(loggingInterceptor)  // Add logging interceptor
            .build()
*/
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(NOTIFICATION_BASE_URL)
                .client(client)  // Attach the custom OkHttp client
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(NotificationApiService::class.java)
        }
    }
}
