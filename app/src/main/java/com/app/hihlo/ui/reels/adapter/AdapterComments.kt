package com.app.hihlo.ui.reels.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.R
import com.app.hihlo.databinding.AdapterCommentsBinding
import com.app.hihlo.model.get_reel_comments.response.Comment
import com.app.hihlo.model.login.response.LoginResponse
import com.app.hihlo.preferences.LOGIN_DATA
import com.app.hihlo.preferences.Preferences
import com.app.hihlo.ui.home.activity.HomeActivity
import com.app.hihlo.ui.reels.bottom_sheet.CommentReelBottomSheet
import com.app.hihlo.utils.CommonUtils
import com.app.hihlo.utils.RTVariable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class AdapterComments(
    var comments: MutableList<Comment>,
    val onReplyClick: (replyText: String, parentCommentId: Int) -> Unit,
    val onDeleteClick: (isReply: Boolean, parentCommentId: Int?, itemId: Int) -> Unit,
    val onReplySelected: (commentId: Int) -> Unit,
    val commentsRecycler: RecyclerView
) : RecyclerView.Adapter<AdapterComments.ViewHolder>() {

    lateinit var adapter: AdapterCommentsReply

    // NEW: Tracks how many replies are currently visible for each comment (0 = collapsed)
    private val visibleReplyCounts = mutableMapOf<Int, Int>()

    inner class ViewHolder(val binding: AdapterCommentsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return comments?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            Log.e("TTTG", "TTG ${comments?.get(position)?.user?.profile_image} ${comments?.get(position)?.user?.username} ${comments?.get(position)?.comment}")

            Glide.with(root.context).load(comments?.get(position)?.user?.profile_image)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.profile_placeholder)
                .into(userImage)

            name.text = comments?.get(position)?.user?.name
            userId.isVisible = false
            comment.text = comments?.get(position)?.comment
            val commentItem = comments[position]
            val commentId = commentItem.id ?: -1
            val totalReplies = commentItem.replies?.size ?: 0
            val visibleCount = visibleReplyCounts.getOrDefault(commentId, 0)
            val displayReplies = if (visibleCount > 0) {
                commentItem.replies?.take(visibleCount) ?: emptyList()
            } else {
                emptyList()
            }

            adapter = AdapterCommentsReply(
                comment_id = comments?.get(position)?.id.toString(),
                replies = displayReplies.toMutableList(),
                onReplySelect = { comment_id ->
                    onReplySelected(comment_id.toInt() ?: -1)
                },
                onDeleteClick = { replyId ->
                    removeItems(
                        mode = 2,
                        commentPosition = position,
                        replyPosition = RTVariable.REPLY_POSITION
                    )
                    onDeleteClick(true, comments?.get(position)?.id, replyId)
                }
            )
            commentReplyRecycler.adapter = adapter
            commentReplyRecycler.isNestedScrollingEnabled = false
            var user = Preferences.getCustomModelPreference<LoginResponse>(root.context, LOGIN_DATA)?.payload?.username
            val commentOwner = comments?.get(position)?.comment_owner
            val commentUser = comments?.get(position)?.user?.username
            val commentOwnerUserName = comments?.get(position)?.post_owner_username
            if (commentOwner == 1 || user == commentUser) {
                if (user == commentOwnerUserName) {
                    delete.isVisible = true
                } else {
                    delete.isVisible = false
                }
            } else {
                delete.isVisible = false
            }
            holder.binding.root.setOnLongClickListener {
                val allowLongClick = commentOwner == 1 || user == commentUser || user == commentOwnerUserName
                if (allowLongClick) {
                    comment.id?.let { id ->
                        RTVariable.COMMENT_POSITION = position
                        onDeleteClick(false, comments?.get(position)?.id, id)
                    }
                    true
                } else {
                    false
                }
            }
            holder.binding.reply.setOnClickListener {
                RTVariable.REPLY_COMBINED_IMAGE_USERNAME = comments?.get(position)?.user?.profile_image + RTVariable.REPLY_COMBINED_IMAGE_DELEMETER + comments?.get(position)?.user?.username + RTVariable.REPLY_COMBINED_IMAGE_DELEMETER
                onReplySelected(comments?.get(position)?.id ?: -1)
            }
            viewMoreLayout.isVisible = totalReplies > 0
            if (totalReplies > 0) {
                val replyText = if (totalReplies == 1) "Reply" else "Replies"
                viewReplies.text = if (visibleCount >= totalReplies) {
                    "Hide $totalReplies $replyText"
                } else {
                    "View $totalReplies $replyText"
                }
            }
            commentReplyRecycler.isVisible = visibleCount > 0
            viewReplies.setOnClickListener {
                val currentComment = comments.getOrNull(position) ?: return@setOnClickListener
                val currentId = currentComment.id ?: return@setOnClickListener
                val total = currentComment.replies?.size ?: 0
                val currentVisible = visibleReplyCounts.getOrDefault(currentId, 0)
                if (currentVisible >= total) {
                    visibleReplyCounts[currentId] = 0
                    notifyItemChanged(position)
                    commentsRecycler.scrollToPosition(position)
                } else {
                    val newVisible = currentVisible + 5
                    visibleReplyCounts[currentId] = if (newVisible > total) total else newVisible
                    notifyItemChanged(position)
                }
            }
        }
    }

    fun updateList(comments: List<Comment>) {
        this.comments.clear()
        this.comments.addAll(comments)
        notifyDataSetChanged()
    }

    fun removeItems(mode: Int, commentPosition: Int, replyPosition: Int = -1) {
        if (mode == 1) {
            comments.removeAt(commentPosition)
            notifyItemRemoved(commentPosition)
        } else if (mode == 2 && replyPosition != -1) {
            val replies = comments[commentPosition].replies?.toMutableList() ?: mutableListOf()
            if (replyPosition < replies.size) {
                replies.removeAt(replyPosition)
            }
            comments[commentPosition].replies = replies
            notifyItemChanged(commentPosition)
        }
    }

    fun addItems(newComments: List<Comment>) {
        val start = comments.size
        comments.addAll(newComments)
        notifyItemRangeInserted(start, newComments.size)
    }

    /*private fun setRichComment(textView: TextView, rawComment: String?) {
        val context = textView.context
        if (rawComment.isNullOrEmpty()) {
            textView.text = ""
            return
        }
        if (!rawComment.contains(RTVariable.REPLY_COMBINED_IMAGE_DELEMETER)) {
            textView.text = rawComment
            return
        }
        val parts = rawComment.split(RTVariable.REPLY_COMBINED_IMAGE_DELEMETER)
        if (parts.size != 3) {
            textView.text = rawComment
            return
        }
        val imageUrl = parts[0].trim()
        val mention = parts[1].trim()
        val restText = parts[2].trim()

        val builder = SpannableStringBuilder()
        builder.append(" ")
        builder.append(" ")
        builder.append(mention)
        builder.append(" ")
        builder.append(restText)

        textView.text = builder

        val targetSize = textView.textSize.toInt()

        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .placeholder(R.drawable.profile_placeholder)
            .error(R.drawable.profile_placeholder)
            .circleCrop()
            .into(object : CustomTarget<Bitmap>(targetSize - 10, targetSize) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val drawable = BitmapDrawable(context.resources, resource)
                    val fontMetrics = textView.paint.fontMetricsInt
                    val lineHeight = fontMetrics.bottom - fontMetrics.top
                    val verticalOffset = (lineHeight - targetSize) / 2

                    drawable.setBounds(0, verticalOffset + 5, targetSize, verticalOffset + targetSize)

                    val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)

                    val spannable = SpannableStringBuilder(textView.text)
                    spannable.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    val mentionStart = 2
                    val mentionEnd = mentionStart + mention.length
                    spannable.setSpan(
                        ForegroundColorSpan(Color.parseColor("#B90A66")),
                        mentionStart,
                        mentionEnd,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    textView.text = spannable
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    } */

}