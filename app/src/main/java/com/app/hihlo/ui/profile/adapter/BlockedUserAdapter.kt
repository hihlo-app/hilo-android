package com.app.hihlo.ui.profile.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.databinding.AdapterFollowersBinding
import com.app.hihlo.databinding.ItemBlockedUsersBinding
import com.app.hihlo.model.blocked_userlist.BlockedUsersResponse.Payload.BlockedUser

class BlockedUserAdapter(
    val getSelectedUser: (userId: String) -> Unit,
    val blockedUsers: List<BlockedUser>,
) :
    RecyclerView.Adapter<BlockedUserAdapter.ViewHolder>() {
    var from = ""

    inner class ViewHolder(var binding: ItemBlockedUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


    override fun getItemCount(): Int {
        return blockedUsers?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBlockedUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
                name.text = blockedUsers[position].userDetails.name
                userName.text = blockedUsers[position].userDetails.username
                userLocation.text = blockedUsers[position].userDetails.city+", "+blockedUsers[position].userDetails.country

            followButton.text = "Unblock"

            followButton.setOnClickListener {
                getSelectedUser(blockedUsers[position].blockedUserId)
            }
        }
    }


}