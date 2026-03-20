package com.app.hihlo.ui.crop_video

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.app.hihlo.R
import com.app.hihlo.databinding.ActivityVideoCroppingBinding
import com.app.hihlo.ui.home.fragment.HomeFragment
import com.redevrx.video_trimmer.event.OnVideoEditedEvent

class VideoCroppingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityVideoCroppingBinding
    var trimUri = ""
    val EXTRA_CROPPED_URI = "cropped_video_uri_extra"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCroppingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // It's good practice to handle nullable types safely
        val videoUriString = intent.getStringExtra("videoUrl")
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
                    trimUri = uri.toString()
                    val resultIntent = Intent().apply {
                        putExtra(EXTRA_CROPPED_URI, trimUri)
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }

                override fun onError(message: String) {
                    Log.e("VideoCroppingActivity", "Error during trimming: $message")
                }

                override fun onProgress(percentage: Int) {
                    Log.d("VideoCroppingActivity", "Trimming progress: $percentage%")
                }
            })
            setDestinationPath(path.absolutePath)
            setVideoInformationVisibility(true)
            setMaxDuration(30) // Example: Max duration 60 second
            setMinDuration(0)  // Example: Min duration 0 seconds
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.btnDone.setOnClickListener {
            binding.videoTrimmer.saveVideo()
        }

    }
}