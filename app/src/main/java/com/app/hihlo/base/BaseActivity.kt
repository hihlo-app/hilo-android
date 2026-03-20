package com.app.hihlo.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.app.hihlo.R

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {

    private var _binding: DB? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId = getLayoutId()
        if (layoutId != 0) {
            val viewBinding = DataBindingUtil.setContentView<DB>(this, layoutId)
            _binding = viewBinding
            viewBinding.lifecycleOwner = this
        }
        setTransparentStatusBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun getLayoutId(): Int

    private fun setTransparentStatusBar() {
        window.apply {
            statusBarColor = Color.TRANSPARENT
            decorView.setBackgroundColor(ContextCompat.getColor(context, R.color.black))

            // Enable edge-to-edge mode
            WindowCompat.setDecorFitsSystemWindows(this, false)

            // Apply window insets listener using ViewCompat for backward compatibility
            ViewCompat.setOnApplyWindowInsetsListener(decorView) { view, insets ->
                val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

                // Check if 3-button navigation is enabled
                if (isThreeButtonNavigation()) {
                    view.setPadding(0, 0, 0, systemBarsInsets.bottom) // Add padding only for 3-button mode
                } else {
                    view.setPadding(0, 0, 0, 0) // No extra padding in gesture mode
                }

                insets
            }

            updateStatusBarIcons(isColorLight(getBackgroundColorInt(decorView)))
        }
    }

    private fun isThreeButtonNavigation(): Boolean {
        val resourceId = resources.getIdentifier("config_navBarInteractionMode", "integer", "android")
        return if (resourceId > 0) {
            resources.getInteger(resourceId) == 0  // 0 = 3-button navigation mode
        } else {
            false // Default to false (gesture mode)
        }
    }

    fun updateStatusBarIcons(isLightBackground: Boolean) {
        // Use WindowInsetsControllerCompat to adjust the status bar icon appearance
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = isLightBackground
            isAppearanceLightNavigationBars = isLightBackground
        }
    }

    fun isColorLight(color: Int): Boolean {
        val darkness = 1 - (0.299 * ((color shr 16) and 0xFF) +
                0.587 * ((color shr 8) and 0xFF) +
                0.114 * (color and 0xFF)) / 255
        return darkness < 0.5
    }

    // Helper function to get the background color of a view
    private fun getBackgroundColorInt(view: View): Int {
        val background = view.background
        return if (background is ColorDrawable) {
            background.color
        } else {
            Color.WHITE // Default color if no background is set
        }
    }
}
