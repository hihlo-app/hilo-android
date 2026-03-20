package com.app.hihlo.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.databinding.AdapterPredefinedMessageHorizontalBinding
import com.app.hihlo.model.predefined_chats.PredefinedChatsResponse

class AdapterPredefinedMessageHorizontal(
    val list: List<PredefinedChatsResponse.Payload>,
    val getSelectedPredefinedChat: (String) -> Unit
) :
    RecyclerView.Adapter<AdapterPredefinedMessageHorizontal.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(AdapterPredefinedMessageHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.binding.apply {
            predefinedText.text = list[position].predefined_chat
            root.setOnClickListener {
                getSelectedPredefinedChat(list[position].predefined_chat.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: AdapterPredefinedMessageHorizontalBinding): RecyclerView.ViewHolder(binding.root)
}