package com.app.hihlo.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.databinding.AdapterCoinsTypeBinding
import com.app.hihlo.model.recharge_package.response.RechargePackageListResponse

class AdapterSendCoinsBottomSheet(
    val list: List<RechargePackageListResponse.Payload>,
    val getSelected: (RechargePackageListResponse.Payload) -> Unit
) : RecyclerView.Adapter<AdapterSendCoinsBottomSheet.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(AdapterCoinsTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.binding.apply {
            noOfCoins.text = "${list[position].coins} coins"
            root.setOnClickListener {
                getSelected(list[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: AdapterCoinsTypeBinding): RecyclerView.ViewHolder(binding.root)
}