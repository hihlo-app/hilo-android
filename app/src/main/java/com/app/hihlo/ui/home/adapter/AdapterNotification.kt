package com.app.hihlo.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.R
import com.app.hihlo.databinding.AdapterNotificationBinding
import com.app.hihlo.model.notification.response.GetNotificationListResponse
import com.app.hihlo.utils.CommonUtils
import com.bumptech.glide.Glide

class AdapterNotification(val list: MutableList<GetNotificationListResponse.Payload>, val onItemClick: (GetNotificationListResponse.Payload) -> Unit) : RecyclerView.Adapter<AdapterNotification.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(AdapterNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.binding.apply {
            title.text = list[position].title
            description.text = list[position].message
            time.text = CommonUtils.formatTime(list[position].created_at, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd  h:mm a")
            Glide.with(holder.itemView.context).load(list[position].profile_image).placeholder(R.drawable.profile_placeholder).error(R.drawable.profile_placeholder).into(notificationIcon)
            Glide.with(holder.itemView.context).load(getNotificationIcon(list[position].notification_type)).into(notificationTypeIcon)
            root.setOnClickListener {
                onItemClick(list[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun updateList(list:List<GetNotificationListResponse.Payload>){
        var start = if (this.list.isNotEmpty())
            this.list.size else 0
        this.list.addAll(list)
        notifyItemRangeInserted(start, this.list.size)
    }
    fun clearList(){
        var size = list.size
        list.clear()
        notifyItemRangeRemoved(0, size)
    }
    fun updateCompleteNewList(list:List<GetNotificationListResponse.Payload>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding: AdapterNotificationBinding): RecyclerView.ViewHolder(binding.root)

    fun getNotificationIcon(notificationType: String): Int{
        return when(notificationType){
            "FOLLOW" -> R.drawable.add_friend
            "REEL_LIKE" -> R.drawable.unlike_icon_reel
            "COMMENT" -> R.drawable.comment_icon_white
            "COMMENT_REPLY" -> R.drawable.comment_icon_white
            "GIFT_RECEIVED" -> R.drawable.gift_icon_white
            "INCOMING_CALL", "Call_Ended" -> R.drawable.call_icon_white
            else -> R.drawable.notification_icon
        }
    }
}