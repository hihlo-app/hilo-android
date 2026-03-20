package com.app.hihlo.ui.profile.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.R
import com.app.hihlo.databinding.AdapterWalletHistoryBinding
import com.app.hihlo.model.wallet_history.WalletHistoryResponse
import com.app.hihlo.utils.CommonUtils
import com.bumptech.glide.Glide

class AdapterWalletHistory(
    val history: List<WalletHistoryResponse.Payload.History>,
    val user: WalletHistoryResponse.Payload.User
) : RecyclerView.Adapter<AdapterWalletHistory.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        Log.d("TAG", "onBggindViewHolder: ${history} ${user}")

        return ViewHolder(AdapterWalletHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        Log.d("TAG", "onBggindViewHolder: ${history} ${user}")

        holder.binding.apply {

            if (history[position].sender_id.isNullOrEmpty()){
                Glide.with(root.context).load(user.profile_image).placeholder(R.drawable.profile_placeholder).error(R.drawable.profile_placeholder).into(userImage)
                name.text = user.name.toString()
//                userName.text = user.name.toString()
                time.text = CommonUtils.formatTime(history[position].created_at.toString(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "h:mm a, dd MMM yyyy")
            }else{
                Glide.with(root.context).load(history[position].user_profile_image).placeholder(R.drawable.profile_placeholder).error(R.drawable.profile_placeholder).into(userImage)
                name.text = history[position].user_name.toString()
                time.text = CommonUtils.formatTime(history[position].created_at.toString(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "h:mm a, dd MMM yyyy")
            }
            noOfCoins.text = history[position].coins.toString()
            when (history[position].transaction_source) {

                "WITHDRAWAL"->{
                    Glide.with(root.context).load(R.drawable.withdraw_icon).into(transactionStatusIcon)
                    when(history[position].transaction_type){
                        "CREDIT" -> {
                            Glide.with(root.context).load(R.drawable.plus_icon_green).into(minusPlusIcon)
                        }
                        "DEBIT"->{
                            Glide.with(root.context).load(R.drawable.minus_icon_orange).into(minusPlusIcon)
                        }
                    }                }
                "GIFT"->{
                    Glide.with(root.context).load(R.drawable.gift_icon_pink).into(transactionStatusIcon)
                    when(history[position].transaction_type){
                        "CREDIT" -> {
                            Glide.with(root.context).load(R.drawable.plus_icon_green).into(minusPlusIcon)
                        }
                        "DEBIT"->{
                            Glide.with(root.context).load(R.drawable.minus_icon_orange).into(minusPlusIcon)
                        }
                    }
                }else -> {
                   when(history[position].transaction_type){
                       "CREDIT" -> {
                           Glide.with(root.context).load(R.drawable.credit_arrow_green).into(transactionStatusIcon)
                           Glide.with(root.context).load(R.drawable.plus_icon_green).into(minusPlusIcon)
                       }
                       "DEBIT"->{
                           Glide.with(root.context).load(R.drawable.debit_arrow_orange).into(transactionStatusIcon)
                           Glide.with(root.context).load(R.drawable.minus_icon_orange).into(minusPlusIcon)
                       }
                   }
                }
            }

        }
    }

    override fun getItemCount() = history.size

    inner class ViewHolder(val binding: AdapterWalletHistoryBinding): RecyclerView.ViewHolder(binding.root)
}