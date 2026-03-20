package com.app.hihlo.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.R
import com.app.hihlo.model.interest_list.response.Interests

class InterestAdapter(
    private var interests: List<Interests>?,
    private val onCitySelected: (Interests) -> Unit
) : RecyclerView.Adapter<InterestAdapter.InterestViewHolder>() {

    inner class InterestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.spinner_dropdown_item, parent, false)
        return InterestViewHolder(view)
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        val city = interests?.get(position) ?: Interests()
        holder.name.text = city.name
        holder.itemView.setOnClickListener {
            onCitySelected(city)
        }
    }

    override fun getItemCount() = interests?.size ?: 0

    fun filterList(filteredList: List<Interests>) {
        this.interests = filteredList
        notifyDataSetChanged()
    }
}