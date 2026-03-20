package com.app.hihlo.ui.reels.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.databinding.AdapterListPopupBinding

class AdapterListPopup(val list: List<String>, private val getSelected: (String) -> Unit): RecyclerView.Adapter<AdapterListPopup.ViewHolder>() {
    inner class ViewHolder(val binding:AdapterListPopupBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterListPopupBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            title.text = list[position]
            if (position==list.size-1){
                bottomLine.isVisible=false
            }
            root.setOnClickListener {
                getSelected(list[position])
            }
        }
    }
}