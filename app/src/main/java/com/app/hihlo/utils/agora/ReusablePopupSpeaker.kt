package com.app.hihlo.utils.agora

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import com.app.hihlo.R
import com.app.hihlo.databinding.LayoutSpeakerPopupBinding
import com.app.hihlo.utils.CommonUtils

class ReusablePopupSpeaker(
    private val context: Context,
    private val anchorView: View,
    private val onOption1Click: () -> Unit,
    private val onOption2Click: () -> Unit,
    private val onOption3Click: (() -> Unit)? = null,
    private val onOption4Click: (() -> Unit)? = null,
    private val option1Text: String = "Option 1",
    private val option2Text: String = "Option 2",
    private val option3Text: String? = null,
    private val option4Text: String? = null,
    private val option1ImageRes: Int? = null,
    private val option2ImageRes: Int? = null,
    private val option3ImageRes: Int? = null,
    private val option4ImageRes: Int? = null,
    private val alignEnd: Boolean? = false,
    private val selectedOption: Int = -1 // 1 for option1, 2 for option2, etc.
) {
    private var popupWindow: PopupWindow? = null

    fun show() {
        val binding = LayoutSpeakerPopupBinding.inflate(LayoutInflater.from(context))

        // Setup Option 1
        binding.tvOption1.text = option1Text
        option1ImageRes?.let { binding.ivOption1.setImageResource(it) }
        binding.ivOption1.visibility = if (option1ImageRes != null) View.VISIBLE else View.GONE
        binding.ivTick1.visibility = if (selectedOption == 1) View.VISIBLE else View.GONE

        binding.tvOption1.setOnClickListener {
            onOption1Click()
            popupWindow?.dismiss()
        }

        // Setup Option 2
        binding.tvOption2.text = option2Text
        option2ImageRes?.let { binding.ivOption2.setImageResource(it) }
        binding.ivOption2.visibility = if (option2ImageRes != null) View.VISIBLE else View.GONE
        binding.ivTick2.visibility = if (selectedOption == 2) View.VISIBLE else View.GONE

        binding.tvOption2.setOnClickListener {
            onOption2Click()
            popupWindow?.dismiss()
        }

        // Setup Option 3
        if (onOption3Click != null && option3Text != null) {
            binding.tvOption3.apply {
                visibility = View.VISIBLE
                text = option3Text
            }
            option3ImageRes?.let { binding.ivOption3.setImageResource(it) }
            binding.ivOption3.visibility = if (option3ImageRes != null) View.VISIBLE else View.GONE
            binding.ivTick3.visibility = if (selectedOption == 3) View.VISIBLE else View.GONE

            binding.tvOption3.setOnClickListener {
                onOption3Click.invoke()
                popupWindow?.dismiss()
            }
        } else {
            binding.tvOption3.visibility = View.GONE
            binding.ivOption3.visibility = View.GONE
            binding.ivTick3.visibility = View.GONE
        }

        // Setup Option 4
        if (onOption4Click != null && option4Text != null) {
            binding.tvOption4.apply {
                visibility = View.VISIBLE
                text = option4Text
            }
            option4ImageRes?.let { binding.ivOption4.setImageResource(it) }
            binding.ivOption4.visibility = if (option4ImageRes != null) View.VISIBLE else View.GONE
            binding.ivTick4.visibility = if (selectedOption == 4) View.VISIBLE else View.GONE

            binding.tvOption4.setOnClickListener {
                onOption4Click.invoke()
                popupWindow?.dismiss()
            }
        } else {
            binding.tvOption4.visibility = View.GONE
            binding.ivOption4.visibility = View.GONE
            binding.ivTick4.visibility = View.GONE
        }

        popupWindow = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            isOutsideTouchable = true
            elevation = 10f
            setOnDismissListener {
                anchorView.setBackgroundColor(Color.TRANSPARENT)
            }
        }

        anchorView.setBackgroundColor(ContextCompat.getColor(context, R.color.white_40))
        anchorView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

        // Measure and position same as before
        binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupHeight = binding.root.measuredHeight
        val popupWidth = binding.root.measuredWidth

        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        val anchorY = location[1]
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels

        val spaceBelow = screenHeight - (anchorY + anchorView.height + CommonUtils.dpToPx(100))
        val shouldShowAbove = spaceBelow < popupHeight
        val centerX = (screenWidth - popupWidth) / 2

        if (shouldShowAbove) {
            popupWindow?.showAtLocation(anchorView, Gravity.TOP or Gravity.START, centerX, anchorY - popupHeight - 15)
        } else {
            popupWindow?.showAtLocation(anchorView, Gravity.TOP or Gravity.START, centerX, anchorY + anchorView.height + 15)
        }
    }
}
