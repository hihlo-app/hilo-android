package com.app.hihlo.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.R
import com.app.hihlo.databinding.ItemAddImageBinding
import com.app.hihlo.ui.home.activity.HomeActivity
import com.app.hihlo.ui.profile.model.ImageItem


class ImageAdapter(
    private val imageList: MutableList<ImageItem>,
    private val onAddImageClick: (Int) -> Unit,
    private val onDeleteImageClick: (Int) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemAddImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemAddImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = imageList[position]

        if (item.imageUri != null) {
            holder.binding.ivImage.setImageURI(item.imageUri)
            holder.binding.ivDelete.visibility = View.VISIBLE
        } else {
            holder.binding.ivImage.setImageResource(R.drawable.add_image)
            holder.binding.ivDelete.visibility = View.GONE
        }

        holder.binding.ivImage.setOnClickListener {
            if (item.imageUri == null) onAddImageClick(position)
        }

        holder.binding.ivDelete.setOnClickListener {
            onDeleteImageClick(position)
        }
    }
}
