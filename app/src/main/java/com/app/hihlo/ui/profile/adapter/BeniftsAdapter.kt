package com.app.hihlo.ui.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.databinding.ItemBenifitsBinding
import com.app.hihlo.model.get_profile.UserDetails
import com.app.hihlo.ui.profile.become_creater.model.Benefits

class BeniftsAdapter(
    private val context: Context?,
) :
    RecyclerView.Adapter<BeniftsAdapter.ViewHolder>() {
    var resultList: List<Benefits>? = null
    var selectedPosition: Int = -1

    inner class ViewHolder(var binding: ItemBenifitsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Benefits, position: Int) {
            binding.apply {
                tvBenifits.text = "${position+1}.  ${data.benefit_text}"
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
        val binding = ItemBenifitsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resultList!![position], position)
    }

    fun setData(it: List<Benefits>?) {
        resultList = it
        notifyDataSetChanged()
    }

}