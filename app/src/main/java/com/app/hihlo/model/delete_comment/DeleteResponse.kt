package com.app.hihlo.model.delete_comment

data class DeleteResponse(
    val code: Int,
    val error: Boolean,
    val message: String,
    val payload: Payload,
    val status: Int
)
