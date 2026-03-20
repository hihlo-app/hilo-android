package com.app.hihlo.network_call

import com.app.hihlo.model.chat.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApiService {
    @Headers("Content-Type: application/json")
    @POST("projects/myapplication-f9f63/messages:send")
    suspend fun postNotification(
        @Header("Authorization") token: String, // Use Bearer Token
        @Body notification: PushNotification
    ): Response<ResponseBody>
}