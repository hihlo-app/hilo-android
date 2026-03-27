package com.app.hihlo.model.GetUserIdResponse

data class GetUserIdByUserNameResponse(
    val error: Boolean,
    val code: Int,
    val status: Int,
    val message: String,
    val payload: Payload
)

data class Payload(
    val user_id: Int
)
