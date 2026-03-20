package com.app.hihlo.ui.home.bottom_sheet

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.app.hihlo.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class IOSStyleBottomSheetDialog(context: Context) : BottomSheetDialog(context, R.style.IOSBottomSheetDialogTheme) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = LayoutInflater.from(context).inflate(R.layout.ios_bottom_sheet, null)
        setContentView(view)

        setupBlurEffect(view)
        setupButtons(view)
        setupBlurredButtons(view) // Replace applyBlurToButtons with this

        // Make background transparent
        window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let { bottomSheet ->
            bottomSheet.background = ColorDrawable(Color.TRANSPARENT)
        }
    }
    private fun setupBlurEffect(view: View) {
        val blurContainer = view.findViewById<LinearLayout>(R.id.blurContainer)

        // Apply blur effect using RenderScript or alternative
        // For API 31+, use alternative blur methods
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            blurContainer.background = ContextCompat.getDrawable(context, R.drawable.ios_blur_background)
        } else {
            // Use RenderScript for older versions
//            blurContainer.background = ContextCompat.getDrawable(context, R.drawable.ios_blur_background_legacy)
        }
    }
    private fun setupBlurredButtons(view: View) {
        // Apply blur only to background views (not text)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val uploadPostBg = view.findViewById<FrameLayout>(R.id.uploadPostFrame)?.getChildAt(0)
            val uploadReelBg = view.findViewById<FrameLayout>(R.id.uploadReelFrame)?.getChildAt(0)
            val cancelBg = view.findViewById<FrameLayout>(R.id.cancelFrame)?.getChildAt(0)

            uploadPostBg?.setRenderEffect(RenderEffect.createBlurEffect(8f, 8f, Shader.TileMode.CLAMP))
            uploadReelBg?.setRenderEffect(RenderEffect.createBlurEffect(8f, 8f, Shader.TileMode.CLAMP))
            cancelBg?.setRenderEffect(RenderEffect.createBlurEffect(8f, 8f, Shader.TileMode.CLAMP))
        }
    }
    private fun applyBlurToButtons(view: View) {
        val uploadPostButton = view.findViewById<TextView>(R.id.btnUploadPost)
        val uploadReelButton = view.findViewById<TextView>(R.id.btnUploadReel)
        val cancelButton = view.findViewById<TextView>(R.id.btnCancel)

        // Apply blur effect to buttons
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            uploadPostButton.setRenderEffect(RenderEffect.createBlurEffect(8f, 8f, Shader.TileMode.CLAMP))
            uploadReelButton.setRenderEffect(RenderEffect.createBlurEffect(8f, 8f, Shader.TileMode.CLAMP))
            cancelButton.setRenderEffect(RenderEffect.createBlurEffect(8f, 8f, Shader.TileMode.CLAMP))
        }
    }
    /*private fun setupBlurredButtons(view: View) {
        val uploadPostButton = view.findViewById<TextView>(R.id.btnUploadPost)
        val uploadReelButton = view.findViewById<TextView>(R.id.btnUploadReel)
        val cancelButton = view.findViewById<TextView>(R.id.btnCancel)

        // Create blurred background views
        createBlurredBackground(uploadPostButton, false)
        createBlurredBackground(uploadReelButton, false)
        createBlurredBackground(cancelButton, true)
    }*/
    private fun createBlurredBackground(textView: TextView, isCancel: Boolean) {
        val parent = textView.parent as ViewGroup
        val index = parent.indexOfChild(textView)

        // Create background view with blur
        val backgroundView = View(context)
        backgroundView.layoutParams = textView.layoutParams
        backgroundView.background = if (isCancel) {
            ContextCompat.getDrawable(context, R.drawable.ios_cancel_blur_background)
        } else {
            ContextCompat.getDrawable(context, R.drawable.ios_button_blur_background)
        }

        // Apply blur only to background (Android 12+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            backgroundView.setRenderEffect(RenderEffect.createBlurEffect(12f, 12f, Shader.TileMode.CLAMP))
        }

        // Make text view background transparent
        textView.background = null
        textView.elevation = 8f

                // Add background view behind text view
                parent.addView(backgroundView, index)
    }


    private fun setupButtons(view: View) {
        val uploadPostButton = view.findViewById<TextView>(R.id.btnUploadPost)
        val uploadReelButton = view.findViewById<TextView>(R.id.btnUploadReel)
        val cancelButton = view.findViewById<TextView>(R.id.btnCancel)

        uploadPostButton.setOnClickListener {
            // Handle upload post
            dismiss()
        }

        uploadReelButton.setOnClickListener {
            // Handle upload reel
            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }
    }
}