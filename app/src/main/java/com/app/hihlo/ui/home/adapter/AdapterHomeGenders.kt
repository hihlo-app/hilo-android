package com.app.hihlo.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.databinding.AdapterHomeGendersBinding
import com.app.hihlo.model.gender_list.Gender

class AdapterHomeGenders(val list: List<Gender>, val getSelectedItem: (Int,String) -> Unit): RecyclerView.Adapter<AdapterHomeGenders.ViewHolder>() {
    inner class ViewHolder(val binding:AdapterHomeGendersBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterHomeGendersBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if (position==list.size-1){
//                filterGender.setTextColor(root.resources.getColor(R.color.white))
                view.isVisible=false
//                filterGender.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(root.context, R.drawable.white_arrow_down), null)
            }else{
//                filterGender.setTextColor(root.resources.getColor(R.color.black))
                view.isVisible=true
//                filterGender.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
            filterGender.text = list[position].gender_name
            root.setOnClickListener {
                getSelectedItem(list[position].id,list[position].gender_name)
            }
        }
    }
}