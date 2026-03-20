package com.app.hihlo.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.databinding.AdapterViewAdsBinding
import com.app.hihlo.model.ads_list.GetAdsListResponse
import com.bumptech.glide.Glide

class AdapterAdsList(val ads: List<GetAdsListResponse.Payload.Ad>, val getSelected: (GetAdsListResponse.Payload.Ad) -> Unit) : RecyclerView.Adapter<AdapterAdsList.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(AdapterViewAdsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.binding.apply {
            Glide.with(root.context).load(ads[position].imageUrl).into(adImage)

            root.setOnClickListener {
                getSelected(ads[position])
            }
        }
    }

    override fun getItemCount() = ads.size

    inner class ViewHolder(val binding: AdapterViewAdsBinding): RecyclerView.ViewHolder(binding.root)
}