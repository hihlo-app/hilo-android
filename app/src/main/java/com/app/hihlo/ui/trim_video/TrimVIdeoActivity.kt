package com.app.hihlo.ui.trim_video

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.media3.exoplayer.ExoPlayer
import com.app.hihlo.R
import com.app.hihlo.base.BaseActivity
import com.app.hihlo.databinding.ActivityRechargeCoinsBinding
import com.app.hihlo.databinding.ActivityTrimVideoBinding
import com.app.hihlo.preferences.UserPreference
import com.app.hihlo.ui.home.fragment.HomeFragment
import com.app.hihlo.ui.profile.fragment.ProfileFragment.Companion.EXTRA_CROPPED_URI
import com.app.hihlo.utils.network_utils.ProcessDialog
import com.redevrx.video_trimmer.event.OnVideoEditedEvent
import com.redevrx.video_trimmer.view.RangeSeekBarView
import java.io.File

class TrimVideoActivity : BaseActivity<ActivityTrimVideoBinding>() {
//    private lateinit var binding:ActivityTrimVideoBinding
override fun getLayoutId(): Int {
    return R.layout.activity_trim_video // Ensure this points to the correct layout resource
}
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrimVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // It's good practice to handle nullable types safely
        val videoUriString = intent.getStringExtra("videoUrl")
        val from = intent.getStringExtra("from") ?: ""
        val videoUri: Uri? = videoUriString?.toUri()

        if (videoUri == null) {
            Log.e("VideoCroppingActivity", "Video URI is null. Cannot proceed with trimming.")
            // You might want to show a toast or finish the activity here
            finish()
            return
        }

        Log.e("VideoCroppingActivity", "onCreate: Video URI received: $videoUri")
        val path =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        binding.videoTrimmer.apply {
            setVideoBackgroundColor(resources.getColor(R.color.black))
            setVideoURI(videoUri)
            setOnTrimVideoListener(object :OnVideoEditedEvent{
                override fun getResult(uri: Uri) {
                    ProcessDialog.dismissDialog(true)
                    Log.e("VideoCroppingActivity", "Error during trimming: result")
//                    binding.dialogBar.isVisible=false

                    UserPreference.seletedUri = uri
                    val resultIntent = Intent().apply {
                        putExtra(EXTRA_CROPPED_URI, "") // You can also put the Uri directly
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }

                override fun onError(message: String) {
                    ProcessDialog.dismissDialog(true)
//                        binding.dialogBar.isVisible=false
                  //  enableClick()
                    Toast.makeText(this@TrimVideoActivity, "Some error occurred! Please try again.", Toast.LENGTH_SHORT).show()
                    Log.e("VideoCroppingActivity", "Error during trimming: $message")
                }

                override fun onProgress(percentage: Int) {
//                        binding.dialogBar.isVisible=true

                    Log.d("VideoCroppingActivity", "Trimming progress: $percentage%")
                }
            })
            setDestinationPath(path.absolutePath)
            setVideoInformationVisibility(true)
            setMaxDuration(if (from=="home") 15 else 60) // Example: Max duration 60 second
            setMinDuration(0)  // Example: Min duration 0 seconds
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.btnDone.setOnClickListener {
            ProcessDialog.showDialog(this, true)
//            binding.dialogBar.isVisible=true
            binding.videoTrimmer.saveVideo()
        }

    }
*/

    override fun onResume() {
        super.onResume()

        this?.window?.let { window ->
            // Explicit black nav bar
            window.navigationBarColor = ContextCompat.getColor(this, R.color.black_1c1c1c)

            // Force nav buttons white
            WindowInsetsControllerCompat(window, window.decorView)
                .isAppearanceLightNavigationBars = false
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityTrimVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoUriString = intent.getStringExtra("videoUrl")
        val from = intent.getStringExtra("from") ?: ""
        val videoUri: Uri? = videoUriString?.toUri()

        if (videoUri == null) {
            Log.e("VideoCroppingActivity", "Video URI is null. Cannot proceed with trimming.")
            finish()
            return
        }

        // ✅ Check resolution before loading into trimmer
        try {
            val retriever = android.media.MediaMetadataRetriever()
            retriever.setDataSource(this, videoUri)
            val width = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt() ?: 0
            val height = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt() ?: 0
            retriever.release()

            Log.d("VideoCroppingActivity", "Video resolution: ${width}x$height")

            /*if (width > 1920 || height > 1080) {
                Toast.makeText(
                    this,
                    "4K videos are not supported. Please select a video with 1080p resolution or lower.",
                    Toast.LENGTH_LONG
                ).show()
                finish()
                return
            }*/
            if (width > 3840 || height > 2160) {
                Toast.makeText(
                    this,
                    "Videos above 4K resolution are not supported.",
                    Toast.LENGTH_LONG
                ).show()
                finish()
                return
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        binding.videoTrimmer.apply {
            setVideoBackgroundColor(resources.getColor(R.color.black))
            setVideoURI(videoUri)
            setOnTrimVideoListener(object : OnVideoEditedEvent {
                override fun getResult(uri: Uri) {
                    ProcessDialog.dismissDialog(true)
                    Log.e("VideoCroppingActivity", "Trim result received")

                    UserPreference.seletedUri = uri
                    val resultIntent = Intent().apply {
                        putExtra(EXTRA_CROPPED_URI, "")
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }

                override fun onError(message: String) {
                    ProcessDialog.dismissDialog(true)
                    Toast.makeText(this@TrimVideoActivity, "Some error occurred! Please try again.", Toast.LENGTH_SHORT).show()
                    Log.e("VideoCroppingActivity", "Error during trimming: $message")
                }

                override fun onProgress(percentage: Int) {
                    Log.d("VideoCroppingActivity", "Trimming progress: $percentage%")
                }
            })
            setDestinationPath(path.absolutePath)
            setVideoInformationVisibility(true)
            setMaxDuration(if (from == "home") 15 else 60)
            setMinDuration(0)
        }

        binding.backButton.setOnClickListener { onBackPressed() }
        binding.btnDone.setOnClickListener {
            ProcessDialog.showDialog(this, true)
            binding.videoTrimmer.saveVideo()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseTrimmerPlayer()
    }

    private fun releaseTrimmerPlayer() {
        try {
            val field = binding.videoTrimmer.javaClass.getDeclaredField("mPlayer")
            field.isAccessible = true
            val exoPlayer = field.get(binding.videoTrimmer) as? ExoPlayer
            exoPlayer?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}