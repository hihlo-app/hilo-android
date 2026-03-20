package com.app.hihlo.model.get_reel_comments.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Comment(
    val comment: String,
    val created_at: String,
    val id: Int,
    val comment_owner: Int,
    val post_owner_username: String,
    var replies: List<Replies>,
    val updated_at: String,
    val user: User
):Parcelable