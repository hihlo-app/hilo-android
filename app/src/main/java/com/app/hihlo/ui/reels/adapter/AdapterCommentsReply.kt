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
import com.app.hihlo.databinding.AdapterCommentsReplyBinding
import com.app.hihlo.model.get_reel_comments.response.Replies
import com.app.hihlo.model.login.response.LoginResponse
import com.app.hihlo.preferences.LOGIN_DATA
import com.app.hihlo.preferences.Preferences
import com.app.hihlo.ui.home.activity.HomeActivity
import com.app.hihlo.utils.RTVariable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class AdapterCommentsReply(
    var comment_id: String,
    var replies: MutableList<Replies>,
    val onReplySelect: (String) -> Unit,               // optional — if you still want inline reply
    val onDeleteClick: (replyId: Int) -> Unit,
    val onReplyProfileSelected: (user_id: Int) -> Unit,// ← new
) : RecyclerView.Adapter<AdapterCommentsReply.ViewHolder>() {
    inner class ViewHolder(val binding: AdapterCommentsReplyBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterCommentsReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return replies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            Glide.with(root.context)
                .load(replies[position].user.profile_image)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.profile_placeholder).into(userImage)
            name.text = replies[position].user.name
//            userId.text = replies[position].user.username
            userId.isVisible=false
            var user_id = replies?.get(position)?.user?.id
//            userLocation.text = replies[position]?.user?.city+", "+replies[position]?.user?.country
            //comment.text = replies[position].reply
            //var data = "https://d38vqutibeq2uv.cloudfront.net/1757159096618####@@@@####@hihlo####@@@@####hloo you"
            setRichComment(comment, replies[position].reply)
            var user = Preferences.getCustomModelPreference<LoginResponse>(root.context, LOGIN_DATA)?.payload?.username
            val commentOwner = replies?.get(position)?.comment_owner
            val commentUser = replies?.get(position)?.user?.username
            val commentOwnerUserName = replies?.get(position)?.post_owner_username
            if (commentOwner == 1 || user == commentUser) {
                if(user == commentOwnerUserName){
                    reply.isVisible = true
                }else{
                    if(user == commentUser){
                        reply.isVisible = false
                    } else {
                        reply.isVisible = true
                    }
                }
            } else {
                reply.isVisible = true
            }
            holder.binding.root.setOnLongClickListener {
                val allowLongClick =
                    commentOwner == 1 || user == commentUser || user == commentOwnerUserName
                if (allowLongClick) {
                    RTVariable.COMMENT_POSITION = position
                    RTVariable.REPLY_POSITION = position
                    replies[position].id?.let { replyId ->
                        onDeleteClick(replyId)
                    }
                    true
                } else {
                    false
                }
            }
            holder.binding.reply.setOnClickListener {
                RTVariable.REPLY_COMBINED_IMAGE_USERNAME = replies?.get(position)?.user?.profile_image + RTVariable.REPLY_COMBINED_IMAGE_DELEMETER + replies?.get(position)?.user?.username + RTVariable.REPLY_COMBINED_IMAGE_DELEMETER
                onReplySelect(comment_id)
            }
            holder.binding.userImage.setOnClickListener {
                user_id?.let { p1 -> onReplyProfileSelected(p1) }
            }
            holder.binding.name.setOnClickListener {
                user_id?.let { p1 -> onReplyProfileSelected(p1) }
            }
//            holder.binding.delete.setOnClickListener {
//                RTVariable.COMMENT_POSITION = position
//                RTVariable.REPLY_POSITION = position
//                replies[position].id?.let { replyId ->
//                    onDeleteClick(replyId)
//                }
//            }
            /*sendButton.setOnClickListener {
                getSelectedReply(replyInput.text.toString())
            }*/
        }
    }

    fun removeItems(position: Int){
        Log.e("REPLYLOG", "REPLYLOG>>> REPLY "+position)
        replies.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun setRichComment(textView: TextView, rawComment: String?) {
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
            .into(object : CustomTarget<Bitmap>(targetSize-10, targetSize) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val drawable = BitmapDrawable(context.resources, resource)
                    val fontMetrics = textView.paint.fontMetricsInt
                    val lineHeight = fontMetrics.bottom - fontMetrics.top
                    val verticalOffset = (lineHeight - targetSize) / 2
                    drawable.setBounds(0, verticalOffset+5, targetSize, verticalOffset + targetSize)
                    val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
                    val spannable = SpannableStringBuilder(textView.text)
                    spannable.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    val mentionStart = 2                                 // after image + space
                    val mentionEnd = mentionStart + mention.length
                    spannable.setSpan(
                        ForegroundColorSpan(Color.parseColor("#B90A66")),
                        mentionStart,
                        mentionEnd,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    textView.text = spannable
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}