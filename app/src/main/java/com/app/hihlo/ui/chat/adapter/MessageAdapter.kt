package com.app.hihlo.ui.chat.adapter


import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.hihlo.R
import com.app.hihlo.databinding.AdapterChatOtherAudioBinding
import com.app.hihlo.databinding.ChatCallingBinding
import com.app.hihlo.databinding.ChatCoinBinding
import com.app.hihlo.databinding.ChatMediaLeftBinding
import com.app.hihlo.databinding.ChatMediaRightBinding
import com.app.hihlo.databinding.ChatitemleftBinding
import com.app.hihlo.databinding.ChatitemrightBinding
import com.app.hihlo.model.chat.Messages
import com.app.hihlo.utils.ChatUtils
import com.app.hihlo.utils.ChatUtils.checkMessageStatus
import com.app.hihlo.enum.MediaType
import com.app.hihlo.utils.CommonUtils
import com.bumptech.glide.Glide
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import kotlin.math.abs

class
MessageAdapter(
    private val getSelectedItem: (String, String, Messages, Int) -> Unit,
    private val listType: String,
    private val onLongTap: (Messages, View, Boolean) -> Unit,
    val currentUserId: Int?,
    val closeKeyboard:()->Unit,
    val audioPlayInterface: AudioPlayInterface,
    private val swipeListener: OnMessageSwipeListener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listOfMessage = mutableListOf<Messages>()

    private val LEFT_TEXT = 0
    private val RIGHT_TEXT = 1
    private val LEFT_MEDIA = 2
    private val RIGHT_MEDIA = 3
    private val VIEW_TYPE_USER_AUDIO = 4
    private val VIEW_TYPE_OTHER_AUDIO = 5
    private val VIEW_TYPE_COIN = 7
    private val VIEW_TYPE_CALLING = 8
    private var previousActivePos=-1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            LEFT_TEXT -> LeftTextViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.chatitemleft, parent, false
                )
            )

            RIGHT_TEXT -> RightTextViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.chatitemright, parent, false
                )
            )
            LEFT_MEDIA -> LeftMediaViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.chat_media_left, parent, false
                )
            )
            RIGHT_MEDIA -> RightMediaViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.chat_media_right, parent, false
                )
            )
            VIEW_TYPE_USER_AUDIO -> {
                UserAudioViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.adapter_chat_other_audio, parent, false
                    )
                )
            }

            VIEW_TYPE_OTHER_AUDIO -> {
                OtherAudioViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.adapter_chat_other_audio, parent, false
                    )
                )
            }
            VIEW_TYPE_COIN -> {
                CoinViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.chat_coin, parent, false
                    )
                )
            }
            VIEW_TYPE_CALLING -> {
                CallingViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.chat_calling, parent, false
                    )
                )
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount() = listOfMessage.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RightTextViewHolder ->{
                holder.bind(listOfMessage[position])
                setDay(holder, position)
            }
            is LeftTextViewHolder ->{
                holder.bind(listOfMessage[position])
                setDay(holder, position)
            }
            is RightMediaViewHolder ->{
                holder.bind(listOfMessage[position])
                setDay(holder, position)
            }
            is LeftMediaViewHolder ->{
                holder.bind(listOfMessage[position])
                setDay(holder, position)
            }
            is UserAudioViewHolder -> {
                holder.bind(listOfMessage[position])
                setDay(holder, position)
            }
            is OtherAudioViewHolder -> {
                holder.bind(listOfMessage[position])
                setDay(holder, position)
            }
            is CoinViewHolder -> {
                holder.bind(listOfMessage[position])
                setDay(holder, position)
            }
            is CallingViewHolder -> {
                holder.bind(listOfMessage[position])
                setDay(holder, position)
            }
        }
    }

    private fun setDay(holder: BaseViewHolder, position: Int) {
        holder.date?.let { date ->
            if (position > 0) {
                if (listOfMessage[position - 1].date != listOfMessage[position].date) {
                    date.isVisible = true
                    date.text = listOfMessage[position].date
                } else {
                    date.isVisible = false
                }
            } else {
                date.isVisible = true
                date.text = listOfMessage[position].date
            }
        }
    }


    inner class RightTextViewHolder(val binding: ChatitemrightBinding) :
        BaseViewHolder(binding.root) {
        override val date: TextView = binding.date
        @SuppressLint("ClickableViewAccessibility")
        fun bind(message: Messages) {
            val gestureDetector =
                GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onLongPress(e: MotionEvent) {
                        onLongTap(message, binding.mainLayout, true)
                    }
                    override fun onSingleTapUp(p0: MotionEvent): Boolean {
                        closeKeyboard()
                        return true
                    }
                })
            binding.root.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                true
            }
            if (message.messageType== MediaType.REPLY.name){
                binding.replyUserMessage.isVisible=true
                binding.replyUserMessage.text = message.repliedMessage
//                binding.replyUserName.text = ""
            }else{
                binding.replyUserMessage.isVisible=false
            }
            binding.showMessage.visibility = View.VISIBLE
            binding.showMessage.text = message.message
//            binding.reaction.text = getUniqueEmojis(message.reaction)
            binding.timeView.text = ChatUtils.convertToTime(message.time ?: "", "yyyy-MM-dd HH:mm:ss", "hh:mm a")
//            if (listType== toggleChatsType[0]){
            Log.i("TAG", "check mess bind: "+message)
                binding.statusIcon.setImageResource(checkMessageStatus(message))
//            }else{
//                binding.statusIcon.isVisible=false
//            }
        }
    }
    inner class UserAudioViewHolder(val binding: AdapterChatOtherAudioBinding)  :
        BaseViewHolder(binding.root){
        override val date: TextView = binding.date
        fun bind(chatMessage: Messages) {
            Log.i("TAG", "bind: "+"user audio")
            val gestureDetector =
                GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onLongPress(e: MotionEvent) {
                        onLongTap(chatMessage, binding.topLayout, true)
                    }
                    override fun onSingleTapUp(p0: MotionEvent): Boolean {
                        closeKeyboard()
                        return true
                    }
                })
            binding.root.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                true
            }
            binding.innerLayout.background = itemView.resources.getDrawable(R.drawable.background_10dp_corner_round)
            binding.innerLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.theme))
            val params = binding.innerLayout.layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
            params.removeRule(RelativeLayout.ALIGN_PARENT_START) // to avoid conflict
            binding.innerLayout.layoutParams = params
//            binding.pause.setBackgroundResource(R.drawable.pause_audio)
//            binding.playIcon.setBackgroundResource(R.drawable.play_audio)
            binding.playerTime.setTextColor(itemView.resources.getColor(R.color.white))
            binding.time.setTextColor(itemView.resources.getColor(R.color.white))
            binding.idSeekBar.thumbTintList = ContextCompat.getColorStateList(binding.root.context, R.color.white)
            binding.idSeekBar.progressBackgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.white)
            binding.idSeekBar.progressTintList = ContextCompat.getColorStateList(binding.root.context, R.color.white)
            binding.idSeekBar.secondaryProgressTintList = ContextCompat.getColorStateList(binding.root.context, R.color.white)
            binding.statusIcon.isVisible = true
            binding.statusIcon.setImageResource(checkMessageStatus(chatMessage))

            binding.playIcon.setOnClickListener {
                audioPlayInterface.onPlayOtherAudio(binding, chatMessage.url ?: "", position, previousActivePos)
                previousActivePos=position
            }
            binding.pause.setOnClickListener {
                audioPlayInterface.onPauseOtherAudio(binding, chatMessage.url ?: "", position, previousActivePos)
            }
            binding.time.text = ChatUtils.convertToTime(chatMessage.time ?: "", "yyyy-MM-dd HH:mm:ss", "hh:mm a")
            binding.playerTime.text = chatMessage.duration ?: ""
            binding.topLayout.setOnClickListener { closeKeyboard() }
        }
    }
    inner class OtherAudioViewHolder(val binding: AdapterChatOtherAudioBinding) :
        BaseViewHolder(binding.root){
        override val date: TextView = binding.date
        fun bind(chatMessage: Messages) {
            Log.i("TAG", "bind: "+"other audio")
            val gestureDetector =
                GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onLongPress(e: MotionEvent) {
                        onLongTap(chatMessage, binding.topLayout, false)
                    }
                    override fun onSingleTapUp(p0: MotionEvent): Boolean {
                        closeKeyboard()
                        return true
                    }
                })
            binding.root.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                true
            }
            binding.innerLayout.background = itemView.resources.getDrawable(R.drawable.background_10dp_corner_round)
            binding.innerLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.white))
            binding.statusIcon.isVisible = false
            binding.time.setTextColor(itemView.resources.getColor(R.color.theme))
            binding.idSeekBar.progressTintList = ContextCompat.getColorStateList(binding.root.context, R.color.theme)
            binding.idSeekBar.secondaryProgressTintList = ContextCompat.getColorStateList(binding.root.context, R.color.theme)
            binding.playIcon.setOnClickListener {
                audioPlayInterface.onPlayOtherAudio(binding, chatMessage.url ?: "", position, previousActivePos)
                previousActivePos=position
            }
            binding.pause.setOnClickListener {
                audioPlayInterface.onPauseOtherAudio(binding, chatMessage.url ?: "", position, previousActivePos)
            }
            binding.time.text = ChatUtils.convertToTime(chatMessage.time ?: "", "yyyy-MM-dd HH:mm:ss", "hh:mm a")
            binding.playerTime.text = chatMessage.duration ?: ""
            binding.topLayout.setOnClickListener { closeKeyboard() }
        }
    }
    inner class LeftMediaViewHolder(val binding: ChatMediaLeftBinding) :
        BaseViewHolder(binding.root) {
        override val date: TextView = binding.date
        fun bind(message: Messages) {
            val gestureDetector =
                GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onLongPress(e: MotionEvent) {
                        onLongTap(message, binding.mainLayout, false)
                    }
                    override fun onSingleTapUp(p0: MotionEvent): Boolean {
                        getSelectedItem(message.message ?: "", message.messageType ?: "", message, 1)
                        return true
                    }
                })
            binding.root.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                true
            }
//            binding.reaction.setOnClickListener {
//                getSelectedItem(message.message ?: "", message.messageType ?: "", message, 1)
//            }
//            binding.reaction.text = getUniqueEmojis(message.reaction)
            binding.timeView.text = ChatUtils.convertToTime(message.time ?: "", "yyyy-MM-dd HH:mm:ss", "hh:mm a")
            when(message.messageType) {
                MediaType.IMAGE.name ->{
                    binding.videoIcon.isVisible=false
                    binding.mediaImage.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(binding.root.context).load(message.url).error(R.drawable.placeholder_image).placeholder(R.drawable.placeholder_image).into(binding.mediaImage)
                }
                MediaType.VIDEO.name ->{
                    binding.videoIcon.isVisible=true
                    binding.mediaImage.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(binding.root.context).load(message.message).error(R.drawable.placeholder_image).placeholder(R.drawable.placeholder_image).into(binding.mediaImage)
                }
                MediaType.DOCUMENT.name ->{
                    binding.videoIcon.isVisible=false
                    binding.mediaImage.scaleType = ImageView.ScaleType.FIT_CENTER
                    var mimeType = ""
                    getMimeType(message.message ?: ""){
                        if (it != null) {
                            mimeType = it
                        }
                    }
//                    Glide.with(binding.root.context).load(Utils.getFileIcon(mimeType)).into(binding.mediaImage)
                }
            }
        }
    }
    inner class RightMediaViewHolder(val binding: ChatMediaRightBinding) :
        BaseViewHolder(binding.root) {
        override val date: TextView = binding.date
        fun bind(message: Messages) {
            val gestureDetector =
                GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onLongPress(e: MotionEvent) {
                        onLongTap(message, binding.mainLayout, true)
                    }
                    override fun onSingleTapUp(p0: MotionEvent): Boolean {
                        getSelectedItem(message.message ?: "", message.messageType ?: "", message, 1)
                        return true
                    }
                })
            binding.root.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                true
            }
//            binding.reaction.setOnClickListener {
//                getSelectedItem(message.message ?: "", message.messageType ?: "", message, 1)
//            }
//            binding.reaction.text = getUniqueEmojis(message.reaction)
//            binding.reaction.text = message.reaction["0"]?.emoji
            binding.timeView.text = ChatUtils.convertToTime(message.time ?: "", "yyyy-MM-dd HH:mm:ss", "hh:mm a")
//            if (listType== toggleChatsType[0]){
                binding.statusIcon.setImageResource(checkMessageStatus(message))
//            }else{
//                binding.statusIcon.isVisible=false
//            }
            when(message.messageType) {
                MediaType.IMAGE.name ->{
                    binding.videoIcon.isVisible=false
                    binding.mediaImage.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(binding.root.context).load(message.url).error(R.drawable.placeholder_image).placeholder(R.drawable.placeholder_image).into(binding.mediaImage)
                }
                MediaType.VIDEO.name ->{
                    binding.videoIcon.isVisible=true
                    binding.mediaImage.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(binding.root.context).load(message.message).error(R.drawable.placeholder_image).placeholder(R.drawable.placeholder_image).into(binding.mediaImage)
                }
                MediaType.DOCUMENT.name ->{
                    binding.videoIcon.isVisible=false
                    binding.mediaImage.scaleType = ImageView.ScaleType.FIT_CENTER
                    var mimeType = ""
                    getMimeType(message.message ?: ""){
                        if (it != null) {
                            mimeType = it
                        }
                    }
//                    Glide.with(binding.root.context).load(Utils.getFileIcon(mimeType)).into(binding.mediaImage)
                }
            }
        }
    }
    inner class LeftTextViewHolder(val binding: ChatitemleftBinding) :
        BaseViewHolder(binding.root) {
        override val date: TextView = binding.date
        fun bind(message: Messages) {
            val gestureDetector =
                GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onLongPress(e: MotionEvent) {
                        onLongTap(message, binding.mainLayout, true)
                    }
                    override fun onSingleTapUp(p0: MotionEvent): Boolean {
                        closeKeyboard()
                        return true
                    }
                })
            binding.root.setOnTouchListener { _, event ->
                gestureDetector?.onTouchEvent(event)
                true
            }
            if (message.messageType== MediaType.REPLY.name){
                binding.replyLayout.isVisible=true
                binding.replyUserMessage.text = message.repliedMessage
                binding.replyUserName.text = ""
            }else{
                binding.replyLayout.isVisible=false
            }
//            binding.reaction.setOnClickListener {
//                getSelectedItem(message.message ?: "", message.messageType ?: "", message, 1)
//            }
//            binding.reaction.text = message.reaction["0"]?.emoji
//            binding.reaction.text = getUniqueEmojis(message.reaction)
            binding.showMessage.visibility = View.VISIBLE
            binding.showMessage.text = message.message
            binding.timeView.text = ChatUtils.convertToTime(message.time ?: "", "yyyy-MM-dd HH:mm:ss", "hh:mm a")
        }
    }

    inner class CoinViewHolder(val binding: ChatCoinBinding) :
        BaseViewHolder(binding.root) {
        override val date: TextView = binding.date
        fun bind(message: Messages) {
            val gestureDetector =
                GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onLongPress(e: MotionEvent) {
                        if (currentUserId.toString() == message.sender ) onLongTap(message, binding.mainLayout, true) else onLongTap(message, binding.mainLayout, false)
                    }
                    override fun onSingleTapUp(p0: MotionEvent): Boolean {
                        closeKeyboard()
                        return true
                    }
                })
            binding.root.setOnTouchListener { _, event ->
                gestureDetector?.onTouchEvent(event)
                true
            }
//            binding.reaction.setOnClickListener {
//                getSelectedItem(message.message ?: "", message.messageType ?: "", message, 1)
//            }
//            binding.reaction.text = message.reaction["0"]?.emoji
//            binding.reaction.text = getUniqueEmojis(message.reaction)
//            binding.showMessage.visibility = View.VISIBLE
            binding.timeView.text = ChatUtils.convertToTime(message.time ?: "", "yyyy-MM-dd HH:mm:ss", "hh:mm a")

            if (currentUserId.toString() == message.sender ){
                val paramsConstraintLayout = binding.outerLayout.layoutParams as RelativeLayout.LayoutParams
                paramsConstraintLayout.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
                paramsConstraintLayout.removeRule(RelativeLayout.ALIGN_PARENT_START) // to avoid conflict
                binding.outerLayout.layoutParams = paramsConstraintLayout

                val params = binding.timeView.layoutParams as ConstraintLayout.LayoutParams
                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                params.startToStart = ConstraintLayout.LayoutParams.UNSET
                binding.timeView.layoutParams = params
                binding.numberOfCoins.text = message.message + " Coins Sent"
            }else{
                binding.numberOfCoins.text = message.message + " Coins Received"

            }
        }
    }
    inner class CallingViewHolder(val binding: ChatCallingBinding) :
        BaseViewHolder(binding.root) {
        override val date: TextView = binding.date
        fun bind(message: Messages) {
            val gestureDetector =
                GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onLongPress(e: MotionEvent) {
                        if (currentUserId.toString() == message.sender ) onLongTap(message, binding.mainLayout, true) else onLongTap(message, binding.mainLayout, false)
                    }
                    override fun onSingleTapUp(p0: MotionEvent): Boolean {
                        closeKeyboard()
                        return true
                    }
                })
            binding.root.setOnTouchListener { _, event ->
                gestureDetector?.onTouchEvent(event)
                true
            }
//            binding.reaction.setOnClickListener {
//                getSelectedItem(message.message ?: "", message.messageType ?: "", message, 1)
//            }
//            binding.reaction.text = message.reaction["0"]?.emoji
//            binding.reaction.text = getUniqueEmojis(message.reaction)
//            binding.showMessage.visibility = View.VISIBLE
            if (message.message=="Audio Call"){
                if (message.sender==currentUserId.toString()){
                    binding.callImage.setImageResource(R.drawable.audio_outgoing)
                }else{
                    binding.callImage.setImageResource(R.drawable.audio_incoming)
                }
            }else{
                if (message.sender==currentUserId.toString()){
                    binding.callImage.setImageResource(R.drawable.video_outgoing)
                }else{
                    binding.callImage.setImageResource(R.drawable.video_incoming)
                }            }
            binding.callType.text = message.message
            binding.timeView.text = ChatUtils.convertToTime(message.time ?: "", "yyyy-MM-dd HH:mm:ss", "hh:mm a")

            if (currentUserId.toString() == message.sender ){
                val paramsConstraintLayout = binding.outerLayout.layoutParams as RelativeLayout.LayoutParams
                paramsConstraintLayout.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
                paramsConstraintLayout.removeRule(RelativeLayout.ALIGN_PARENT_START) // to avoid conflict
                binding.outerLayout.layoutParams = paramsConstraintLayout

                val params = binding.timeView.layoutParams as ConstraintLayout.LayoutParams
                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                params.startToStart = ConstraintLayout.LayoutParams.UNSET
                binding.timeView.layoutParams = params
            }
        }
    }
    /*fun getUniqueEmojis(reactionMap: Map<String, Reaction>): String {
        val uniqueEmojis = mutableSetOf<String>() // Use a Set to ensure uniqueness

        for (reaction in reactionMap.values) {
            if (uniqueEmojis.size < 3) {
                uniqueEmojis.add(reaction.emoji) // Add emoji only if unique
            } else {
                break // Stop when we have 3 unique emojis
            }
        }

        return uniqueEmojis.joinToString("") // Concatenate emojis into a string
    }*/

    override fun getItemViewType(position: Int):Int{
        val sender = listOfMessage[position].sender
        val type =  listOfMessage[position].messageType
       val currentUser = currentUserId.toString()
        Log.i("TAG", "getItemViewType: "+sender+"   "+currentUser)

        return if (sender == currentUser && (type==MediaType.TEXT.name||type==MediaType.REPLY.name)) {
           RIGHT_TEXT
       }else if (sender != currentUser && (type==MediaType.TEXT.name||type==MediaType.REPLY.name)){
           LEFT_TEXT
       } else if (sender == currentUser && (type==MediaType.IMAGE.name || type==MediaType.VIDEO.name || type==MediaType.DOCUMENT.name)) {
           RIGHT_MEDIA
       }else if (sender != currentUser && (type==MediaType.IMAGE.name || type==MediaType.VIDEO.name || type==MediaType.DOCUMENT.name)){
           LEFT_MEDIA
       }else if (sender == currentUser && (type==MediaType.AUDIO.name)) {
            VIEW_TYPE_USER_AUDIO
        }else if (sender != currentUser && (type==MediaType.AUDIO.name)){
            VIEW_TYPE_OTHER_AUDIO
        }else if ((type==MediaType.COIN.name)){
            VIEW_TYPE_COIN
        }else if (type==MediaType.CALL.name){
            VIEW_TYPE_CALLING
        }
        else {
           0
       }
        return LEFT_TEXT
    }

    fun setList(newList: MutableList<Messages>) {
        this.listOfMessage = newList
    }
//    fun addOfflineMessage(message:Messages){
//        this.listOfMessage.add(message)
//        notifyDataSetChanged()
//    }
    fun getMimeType(url: String, callback: (String?) -> Unit) {
        val mimeType = getMimeTypeFromUrl(url)
        if (mimeType != null) {
            callback(mimeType)
        } else {
            fetchMimeTypeFromHeader(url, callback)
        }
    }
    private fun getMimeTypeFromUrl(url: String): String? {
        return MimeTypeMap.getFileExtensionFromUrl(url)?.let { extension ->
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
        }
    }

    private fun fetchMimeTypeFromHeader(url: String, callback: (String?) -> Unit) {
        val request = Request.Builder().url(url).head().build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                val contentType = response.header("Content-Type")
                callback(contentType)
            }
        })
    }
    open class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open val date: TextView? = null
    }
    interface AudioPlayInterface {
        fun copyText(text:String)
        fun onPlayOtherAudio(binding: AdapterChatOtherAudioBinding, s: String, position: Int, prevActivePos:Int)
        fun onPauseOtherAudio(binding: AdapterChatOtherAudioBinding, s: String, position: Int, prevActivePos:Int)
    }
    interface OnMessageSwipeListener {
        fun onMessageSwiped(message: Messages)
    }


}

