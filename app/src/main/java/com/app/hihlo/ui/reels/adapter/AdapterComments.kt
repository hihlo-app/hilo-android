package com.app.hihlo.ui.reels.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.R
import com.app.hihlo.databinding.AdapterCommentsBinding
import com.app.hihlo.model.get_reel_comments.response.Comment
import com.app.hihlo.model.login.response.LoginResponse
import com.app.hihlo.preferences.LOGIN_DATA
import com.app.hihlo.preferences.Preferences
import com.app.hihlo.ui.home.activity.HomeActivity
import com.app.hihlo.utils.CommonUtils
import com.app.hihlo.utils.RTVariable
import com.bumptech.glide.Glide

class AdapterComments(
    var comments: MutableList<Comment>,
    val onReplyClick: (replyText: String, parentCommentId: Int) -> Unit,     // existing
    val onDeleteClick: (isReply: Boolean, parentCommentId: Int?, itemId: Int) -> Unit,   // ← new
    val onReplySelected: (commentId: Int) -> Unit                                 // existing (renamed for clarity)
) : RecyclerView.Adapter<AdapterComments.ViewHolder>() {

    lateinit var adapter: AdapterCommentsReply

    inner class ViewHolder(val binding:AdapterCommentsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return comments?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            Glide.with(root.context).load(comments?.get(position)?.user?.profile_image).placeholder(
                R.drawable.profile_placeholder).error(R.drawable.profile_placeholder).into(userImage)
            name.text = comments?.get(position)?.user?.name
//            userId.text = comments?.get(position)?.user?.username
            userId.isVisible=false
//            userLocation.text = comments?.get(position)?.user?.city+", "+comments?.get(position)?.user?.country
            comment.text = comments?.get(position)?.comment
            adapter = AdapterCommentsReply(
                replies = comments[position].replies?.toMutableList() ?: mutableListOf(),
                onReplySubmit = { replyText ->
                    onReplyClick(replyText, comment.id ?: -1)
                },

                onDeleteClick = { replyId ->
                    // We know it's a reply → parent is current comment
                    removeItems(
                        mode = 2,
                        commentPosition = position,
                        replyPosition = RTVariable.REPLY_POSITION
                    )
                    onDeleteClick(true, comment.id, replyId)
                }
            )
            commentReplyRecycler.adapter = adapter
            commentReplyRecycler.isNestedScrollingEnabled = false
            /*sendButton.setOnClickListener {
                getSelectedReply(replyInput.text.toString(), comments?.get(position)?.id ?: -1)
            }*/
            var user = Preferences.getCustomModelPreference<LoginResponse>(root.context, LOGIN_DATA)?.payload?.username
            val commentOwner = comments?.get(position)?.comment_owner
            val commentUser = comments?.get(position)?.user?.username
            val commentOwnerUserName = comments?.get(position)?.post_owner_username
            if (commentOwner == 1 || user == commentUser) {
                if(user == commentOwnerUserName){
                    delete.isVisible = true
                }else{
                    if(user == commentUser){
                        delete.isVisible = false
                    } else {
                        delete.isVisible = false
                    }
                }
            } else {
                delete.isVisible = false
            }
            holder.binding.root.setOnLongClickListener {
                val allowLongClick =
                    commentOwner == 1 || user == commentUser || user == commentOwnerUserName
                if (allowLongClick) {
                    comment.id?.let { id ->
                        RTVariable.COMMENT_POSITION = position
                        onDeleteClick(false, comments?.get(position)?.id, id)   // ← main comment delete
                    }
                    true
                } else {
                    false
                }
            }
//            holder.binding.delete.setOnClickListener {
//                comment.id?.let { id ->
//                    RTVariable.COMMENT_POSITION = position
//                    onDeleteClick(false, comments?.get(position)?.id, id)   // ← main comment delete
//                }
//            }

            holder.binding.reply.setOnClickListener {
                onReplySelected(comments?.get(position)?.id ?: -1)
            }
        }
    }
    fun updateList(comments: List<Comment>) {
        this.comments.clear() // Clear existing comments for full refresh
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
        val start = comments.size // Start position for new items
        comments.addAll(newComments)
        notifyItemRangeInserted(start, newComments.size) // Notify adapter of new items only
    }
}