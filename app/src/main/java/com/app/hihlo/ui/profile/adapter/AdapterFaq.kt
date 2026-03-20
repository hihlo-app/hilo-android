package com.app.hihlo.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.R
import com.app.hihlo.databinding.AdapterFaqBinding
import com.app.hihlo.model.faq.response.FaqResponse

class AdapterFaq(val faqsList: List<FaqResponse.Payload.Faqs>) : RecyclerView.Adapter<AdapterFaq.ViewHolder>() {
    var selectedPosition = -1

    inner class ViewHolder(itemView: AdapterFaqBinding) : RecyclerView.ViewHolder(itemView.root){
        var binding: AdapterFaqBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_faq, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.mainLayout.isFocusable = true
        holder.binding.mainLayout.isClickable = true
        holder.binding.title.text = faqsList[position].question
        holder.binding.description.text = faqsList[position].answer


        if (position != selectedPosition) {
            holder.binding.description.visibility = View.GONE
            holder.binding.expandButton.rotation = 360f
        }else{
            holder.binding.description.visibility = View.VISIBLE
            holder.binding.expandButton.rotation = 180f
        }
        holder.binding.mainLayout.setOnClickListener {
            if (selectedPosition == position) {
                selectedPosition = -1
                notifyItemChanged(position)
            } else {
                val previousPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousPosition) // Collapse old item
                notifyItemChanged(position) // Expand new item
            }
        }

    }

    override fun getItemCount(): Int {
        return faqsList.size
    }
}