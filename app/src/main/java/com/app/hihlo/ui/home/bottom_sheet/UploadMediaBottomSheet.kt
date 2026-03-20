package com.app.hihlo.ui.home.bottom_sheet

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.hihlo.databinding.FragmentUploadMediaBottomSheetBinding
import com.app.hihlo.ui.reels.adapter.AdapterListPopup



class UploadMediaBottomSheet(
    private val context: Context,
    private val previousScreen: String,
    private val parentView: View // Changed from anchorView to parentView for centering
) {
    private var _binding: FragmentUploadMediaBottomSheetBinding? = null
    private val binding get() = _binding!!
    var onGallerySelected: ((Int) -> Unit)? = null
    var onUploadTypeSelected: ((Int) -> Unit)? = null

    private val popupWindow: PopupWindow

    init {
        // Inflate the layout
        _binding = FragmentUploadMediaBottomSheetBinding.inflate(LayoutInflater.from(context))

        // Set up PopupWindow
        popupWindow = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT, // Use WRAP_CONTENT for centering
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            // Set background for rounded corners and outside touch dismissal
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)))
            isOutsideTouchable = true
            isFocusable = true
        }

        // Set up RecyclerView adapter based on previousScreen
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.listPopupRecycler.layoutManager = LinearLayoutManager(context)
        when (previousScreen) {
            "home" -> {
                binding.listPopupRecycler.adapter = AdapterListPopup(homeUploadStoryOptionsList) {
                    when (it) {
                        homeUploadStoryOptionsList[0] -> {
                            onGallerySelected?.invoke(0)
                            dismiss()
                        }
                        homeUploadStoryOptionsList[1] -> {
                            dismiss()
                        }
                    }
                }
            }
            "profile" -> {
                binding.listPopupRecycler.adapter = AdapterListPopup(uploadType) {
                    when (it) {
                        uploadType[0] -> {
                            onGallerySelected?.invoke(0)
                            dismiss()
                        }
                        uploadType[1] -> {
                            onGallerySelected?.invoke(1)
                            dismiss()
                        }
                    }
                }
            }
            "contactUs" -> {
                binding.listPopupRecycler.adapter = AdapterListPopup(homeUploadStoryOptionsList) {
                    when (it) {
                        homeUploadStoryOptionsList[0] -> {
                            onGallerySelected?.invoke(0)
                            dismiss()
                        }
                        homeUploadStoryOptionsList[1] -> {
                            dismiss()
                        }
                    }
                }
            }
            "post" -> {
                binding.listPopupRecycler.adapter = AdapterListPopup(listOf(uploadReelList[0])) {
                    onUploadTypeSelected?.invoke(0)
                    dismiss()
                }
            }
            "reel" -> {
                binding.listPopupRecycler.adapter = AdapterListPopup(listOf(uploadReelList[1])) {
                    onUploadTypeSelected?.invoke(1)
                    dismiss()
                }
            }
        }
    }

    fun show() {
        // Show the PopupWindow at the center of the screen
        popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0)
    }

    fun dismiss() {
        popupWindow.dismiss()
        _binding = null
    }

    companion object {
        // Assuming these lists are defined elsewhere in your codebase
        val homeUploadStoryOptionsList = listOf("Gallery", "Cancel")
        val uploadType = listOf("Photo", "Video")
        val uploadReelList = listOf("Post", "Reel")
    }
}