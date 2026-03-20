package com.app.hihlo.utils
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.isVisible
import com.app.hihlo.databinding.AudioVideoCallPopupMenuBinding
import com.app.hihlo.databinding.LayoutPopupMenuBinding

class ReusableAudioVideoPopup(
    private val context: Context,
    private val anchorView: View,
    private val onAudioClick: () -> Unit,
    private val onVideoClick: () -> Unit,
    private val audioAlpha: Float= 1f,
    private val videoAlpha: Float= 1f,
    private val audioCallText: String? = "Audio Call ",
    private val videoCallText: String? = "Video Call ",
    private val alignEnd: Boolean = false
) {
    private var popupWindow: PopupWindow? = null

    fun show() {
        val binding = AudioVideoCallPopupMenuBinding.inflate(LayoutInflater.from(context))

        binding.tvAudioCall.text = audioCallText
        binding.tvVideoCall.text = videoCallText

        binding.tvAudioCall.alpha = audioAlpha
        binding.ivAudioCall.alpha = audioAlpha
        binding.tvVideoCall.alpha = videoAlpha
        binding.ivVideoCall.alpha = videoAlpha
        binding.audioCoinImage.isVisible = audioAlpha==1f
        binding.videoCoinImage.isVisible = videoAlpha==1f


        binding.tvAudioCall.setOnClickListener {
            onAudioClick()
            popupWindow?.dismiss()
        }

        binding.tvVideoCall.setOnClickListener {
            onVideoClick()
            popupWindow?.dismiss()
        }

        popupWindow = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            isOutsideTouchable = true
            elevation = 10f
        }

        anchorView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

        binding.root.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )
        val popupHeight = binding.root.measuredHeight
        val popupWidth = binding.root.measuredWidth

        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        val anchorY = location[1]
        val anchorX = location[0]
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        val spaceBelow = screenHeight - (anchorY + anchorView.height + CommonUtils.dpToPx(100))
        val shouldShowAbove = spaceBelow < popupHeight

        val xOffset = if (alignEnd) {
            val anchorRight = anchorX + anchorView.width
            anchorRight - popupWidth - anchorX
        } else {
            0
        }

        if (shouldShowAbove) {
            popupWindow?.showAtLocation(
                anchorView,
                Gravity.TOP,
                anchorX + xOffset,
                anchorY - popupHeight - 15
            )
        } else {
            popupWindow?.showAsDropDown(anchorView, xOffset, 15)
        }
    }
}
