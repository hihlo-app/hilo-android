package com.app.hihlo.ui.profile.adapter

import android.R.attr.height
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.databinding.AdapterShowProfileMediaBinding
import com.app.hihlo.model.get_profile.Posts
import com.bumptech.glide.Glide

class AdapterProfileMedia(val posts: Posts, val getSelectedItem: (Int) -> Unit) :RecyclerView.Adapter<AdapterProfileMedia.ViewHolder>() {
    inner class ViewHolder(val binding: AdapterShowProfileMediaBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterShowProfileMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return posts.data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (posts.data[position].asset_type == "V") {
            holder.binding.isVideoIcon.isVisible = true
        } else {
            holder.binding.isVideoIcon.isVisible = false
        }

        holder.binding.apply {
            if (posts.data[position].asset_type == "V") {
                image.updateLayoutParams {
                    height = (200 * holder.binding.root.resources.displayMetrics.density).toInt()
                }
            } else {
                image.updateLayoutParams {
                    height = (140 * holder.binding.root.resources.displayMetrics.density).toInt()
                }
            }
            Glide.with(root.context)
                .load(posts.data[position].asset_url)
                .into(image)

            image.setOnClickListener {
                getSelectedItem(position)
            }
        }
    }
    fun updateList(posts:Posts){
        Log.i("TAG", "updateList: "+posts)
        var start = if (this.posts.data.isNotEmpty())
            this.posts.data.size else 0
        this.posts.data.addAll(posts.data)
        notifyItemRangeInserted(start, this.posts.data.size)
    }
    fun clearList(){
        var size = posts.data.size
        posts.data.clear()
        notifyItemRangeRemoved(0, size)
    }

}