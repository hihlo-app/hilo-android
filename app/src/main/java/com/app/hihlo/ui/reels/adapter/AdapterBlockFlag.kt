package com.app.hihlo.ui.reels.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.R
import com.app.hihlo.databinding.AdapterBlockFlagBinding
import com.app.hihlo.model.block_reasons.response.BlockReason

class AdapterBlockFlag(
    var blockReasons: List<BlockReason>,
    val screen: String,
    val getSelectedReason: (String) -> Unit
) : RecyclerView.Adapter<AdapterBlockFlag.ViewHolder>() {
    inner class ViewHolder(val binding:AdapterBlockFlagBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterBlockFlagBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return blockReasons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if (blockReasons[position].isSelected==true){
                selector.setImageResource(R.drawable.selected_circle)
            }else{
                selector.setImageResource(R.drawable.unselected_circle)
            }
            when(screen){
                "block"->{
                    reason.text = blockReasons.get(position).block_reason
                }
                "flag"->{
                    reason.text = blockReasons.get(position).flag_reason
                }
                "delete_account"->{
                    reason.text = blockReasons.get(position).delete_reason
                }
            }
            root.setOnClickListener {
                unselectOther()
                blockReasons[position].isSelected=true
                getSelectedReason(blockReasons[position].id.toString())
                notifyDataSetChanged()
            }
        }
    }

    private fun unselectOther() {
        blockReasons.map { it.isSelected = false }
    }
}