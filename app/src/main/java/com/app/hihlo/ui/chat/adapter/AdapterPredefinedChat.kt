package com.app.hihlo.ui.chat.adapter

import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.databinding.AdapterPredefinedChatBinding
import com.app.hihlo.model.chat.Messages
import com.app.hihlo.model.predefined_chats.PredefinedChatsResponse

class AdapterPredefinedChat(
    val payload: List<PredefinedChatsResponse.Payload>,
    private val onLongTap: (PredefinedChatsResponse.Payload, view:View) -> Unit,
    val getSelectedChat: (String) -> Unit,
) : RecyclerView.Adapter<AdapterPredefinedChat.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(AdapterPredefinedChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.binding.apply {
            val gestureDetector =
                GestureDetector(root.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onLongPress(e: MotionEvent) {
                        onLongTap(payload[position], root)
                    }
                    override fun onSingleTapUp(p0: MotionEvent): Boolean {
//                        closeKeyboard()
                        getSelectedChat(payload[position].message ?: "")

                        return true
                    }
                })
            root.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                true
            }

            messageNumber.text = "${position+1}."
            showMessage.text = "${payload[position].message}"

        }
    }

    override fun getItemCount()= payload.size

    inner class ViewHolder(val binding: AdapterPredefinedChatBinding): RecyclerView.ViewHolder(binding.root)
}