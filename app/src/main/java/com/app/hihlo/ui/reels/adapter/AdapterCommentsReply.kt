package com.app.hihlo.ui.reels.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
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

class AdapterCommentsReply(
    var replies: MutableList<Replies>,
    val onReplySubmit: (String) -> Unit,               // optional — if you still want inline reply
    val onDeleteClick: (replyId: Int) -> Unit          // ← new
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
            Glide.with(root.context).load(replies[position].user.profile_image).placeholder(
                R.drawable.profile_placeholder).error(R.drawable.profile_placeholder).into(userImage)
            name.text = replies[position].user.name
//            userId.text = replies[position].user.username
            userId.isVisible=false
//            userLocation.text = replies[position]?.user?.city+", "+replies[position]?.user?.country
            comment.text = replies[position].reply
            var user = Preferences.getCustomModelPreference<LoginResponse>(root.context, LOGIN_DATA)?.payload?.username
            val commentOwner = replies?.get(position)?.comment_owner
            val commentUser = replies?.get(position)?.user?.username
            val commentOwnerUserName = replies?.get(position)?.post_owner_username
            if (commentOwner == 1 || user == commentUser) {
                if(user == commentOwnerUserName){
                    delete.isVisible = false
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
}