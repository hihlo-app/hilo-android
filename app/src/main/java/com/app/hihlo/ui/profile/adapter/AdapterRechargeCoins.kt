package com.app.hihlo.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.R
import com.app.hihlo.databinding.AdapterRechargeCoinsBinding
import com.app.hihlo.model.recharge_package.response.RechargePackageListResponse
import com.google.android.material.card.MaterialCardView

class AdapterRechargeCoins(
    val payload: List<RechargePackageListResponse.Payload>,
    val getSelectedCoins: (RechargePackageListResponse.Payload) -> Unit
) : RecyclerView.Adapter<AdapterRechargeCoins.ViewHolder>() {

    private var selectedPosition = 0

    inner class ViewHolder(itemView: AdapterRechargeCoinsBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: AdapterRechargeCoinsBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_recharge_coins,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            coinNumber.text = payload[position].coins.toString()
            additionalAmountTag.text = "Extra ${payload[position].gst_rate}% GST"
            popularTag.alpha = if (payload[position].is_most_popular == 1) 1f else 0f

            // ✅ Apply border color based on selection
            val card = root as MaterialCardView
            if (position == selectedPosition) {
                card.strokeColor = card.context.getColor(R.color.theme)
//                card.strokeWidth = 4  // you can adjust thickness
            } else {
                card.strokeColor = card.context.getColor(R.color.white)
//                card.strokeWidth = 1
            }

            root.setOnClickListener {
                val previous = selectedPosition
                selectedPosition = position
                notifyItemChanged(previous)
                notifyItemChanged(selectedPosition)

                getSelectedCoins(payload[position])
            }
        }
    }

    override fun getItemCount(): Int = payload.size
}
