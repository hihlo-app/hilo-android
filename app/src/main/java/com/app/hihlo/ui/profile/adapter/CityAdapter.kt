package com.app.hihlo.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.R
import com.app.hihlo.model.city_list.response.Cities

class CityAdapter(
    private var cities: List<Cities>,
    private val onCitySelected: (Cities) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.spinner_dropdown_item, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.name.text = city.city_name
        holder.itemView.setOnClickListener {
            onCitySelected(city)
        }
    }

    override fun getItemCount() = cities.size

    fun filterList(filteredList: List<Cities>) {
        this.cities = filteredList
        notifyDataSetChanged()
    }
}
