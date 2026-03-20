package com.app.hihlo.ui.signup.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.R
import com.app.hihlo.databinding.ItemSelectCountryBinding
import com.app.hihlo.model.city_list.response.Cities

class CityAdapter(
    private val context: Context?,
    private val citySelectedListener: OnCitySelectedListener
) :
    RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    var resultList: List<Cities>? = null
    var selectedPosition: Int = -1

    inner class ViewHolder(var binding: ItemSelectCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Cities, position: Int) {
            binding.tvCityName.text = data.city_name
            val isSelected = position == selectedPosition
            binding.btnRadio.setImageResource(
                if (isSelected) R.drawable.checked_radio else R.drawable.unchecked_radio
            )

            binding.root.setOnClickListener {
                updateSelection(position)
            }

            binding.btnRadio.setOnClickListener {
                updateSelection(position)
            }
        }
    }


    override fun getItemCount(): Int {
        if (resultList == null) {
            return 0
        } else {
            return resultList?.size!!
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSelectCountryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    private fun updateSelection(position: Int) {
        val previousSelected = selectedPosition
        selectedPosition = position

        if (previousSelected >= 0) notifyItemChanged(previousSelected, "selection")
        notifyItemChanged(selectedPosition, "selection")

        resultList?.getOrNull(position)?.let {
            citySelectedListener.onCitySelected(it,position)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resultList!![position], position)
    }

    fun setData(it: List<Cities>) {
        resultList = it
        notifyDataSetChanged()
    }

    interface OnCitySelectedListener {
        fun onCitySelected(city: Cities,position: Int)
    }


}