package com.app.hihlo.ui.calling
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import com.app.hihlo.R
import com.app.hihlo.preferences.UserPreference
import com.app.hihlo.ui.calling.activity.OldIncomingCallActivity
import com.app.hihlo.ui.calling.activity.OldOutgoingCallActivity
import com.app.hihlo.ui.calling.activity.OutgoingVideoCallActivity
import com.app.hihlo.ui.calling.activity.SingleVideoCallActivity
import com.app.hihlo.ui.calling.view_model.CallState
import com.app.hihlo.utils.CommonUtils.toIconCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CallForegroundService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1234
        const val CHANNEL_ID = "call_channel"
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val EXTRA_USER_NAME = "extra_user_name"
        const val EXTRA_PROFILE_URL = "extra_profile_url"
        const val CALL_TYPE = "CALL_TYPE"
        const val Call_TIME = "CALL_TIME"
        const val INCOMING_OUTGOING = "INCOMING_OUTGOING"
    }

    private var updateJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val userName = intent.getStringExtra(EXTRA_USER_NAME) ?: "Unknown User"
                val profileUrl = intent.getStringExtra(EXTRA_PROFILE_URL) ?: ""
                val callType = intent.getStringExtra(CALL_TYPE) ?: ""
                val callTime = intent.getStringExtra(Call_TIME)?.toLongOrNull() ?: 0L
                val incoming = intent.getStringExtra(INCOMING_OUTGOING) ?: ""

                startForegroundServiceWithNotification(userName, profileUrl, callType, callTime, incoming)
            }
            ACTION_STOP -> stopForegroundService()
        }
        return START_STICKY
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundServiceWithNotification(
        userName: String,
        profileImageUrl: String,
        callType: String,
        callTime: Long,
        incoming: String
    ) {
        // Load profile image into Bitmap
        val bitmap: Bitmap? = try {
            Glide.with(this@CallForegroundService)
                .asBitmap()
                .load(profileImageUrl)
                .circleCrop()
                .submit(100, 100)
                .get()
        } catch (e: Exception) {
            Log.e("TAG", "Failed to load profile image: ${e.message}")
            null
        }

        Log.e("TAG", "onStartCommand:start $userName \n $profileImageUrl \n $callType \n $callTime $incoming")

        // Create RemoteViews for collapsed and expanded notifications
        val collapsedViews = RemoteViews(packageName, R.layout.outgoing_notification_collapsed).apply {
            setTextViewText(R.id.tvName, userName)
            setTextViewText(R.id.tvTimer, "00:00")
            bitmap?.let { setImageViewBitmap(R.id.ivProfile, it) }
                ?: setImageViewResource(R.id.ivProfile, R.drawable.profile_placeholder)
        }
        val expandedViews = RemoteViews(packageName, R.layout.outgoing_notification_layout).apply {
            setTextViewText(R.id.tvName, userName)
            setTextViewText(R.id.tvDescription, "Ongoing Call")
            setTextViewText(R.id.tvTimer, "00:00")
            bitmap?.let { setImageViewBitmap(R.id.ivProfile, it) }
                ?: setImageViewResource(R.id.ivProfile, R.drawable.profile_placeholder)
        }

        // PendingIntent to reopen call activity
        val activityIntent = Intent(this@CallForegroundService, if (incoming=="incoming") OldIncomingCallActivity::class.java else OutgoingVideoCallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_USER_NAME, userName)
            putExtra(EXTRA_PROFILE_URL, profileImageUrl)
            putExtra(CALL_TYPE, callType)
            putExtra(Call_TIME, callTime.toString())
        }
        val pendingIntent = PendingIntent.getActivity(
            this@CallForegroundService, 0, activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )



        val hangUpActivityIntent = Intent(this, if (incoming=="incoming") OldIncomingCallActivity::class.java else OutgoingVideoCallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("EXTRA_FINISH", true)
        }

        val hangUpPendingIntent = PendingIntent.getActivity(
            this, 1, hangUpActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Ongoing Call",
                NotificationManager.IMPORTANCE_DEFAULT // keep high
            ).apply {
                setSound(null, null)       // 👈 disable sound
                enableVibration(false)     // 👈 disable vibration
                setShowBadge(false)        // optional
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }


        // Build notification
        val notificationBuilder = NotificationCompat.Builder(this@CallForegroundService, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Ensure high priority
            .setOngoing(true)
            .setCustomContentView(collapsedViews)
            .setCustomBigContentView(expandedViews)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)

        // Start foreground service with initial notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val person = Person.Builder()
                .setName(userName)
                .setIcon(profileImageUrl.toIconCompat(this)) // util function
                .build()

            val callStyle = NotificationCompat.CallStyle
                .forOngoingCall(person, hangUpPendingIntent)


            notificationBuilder.setStyle(callStyle)
            notificationBuilder.setCategory(Notification.CATEGORY_CALL)
        }

// Start foreground with correct service type
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notificationBuilder.build(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE or
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
            )
        } else {
            startForeground(NOTIFICATION_ID, notificationBuilder.build())
        }
        // Update notification with running timer
        updateJob?.cancel() // Cancel any existing job
        updateJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                val elapsedTime = (SystemClock.elapsedRealtime() - callTime) / 1000
//                val formattedTime = String.format("%02d:%02d", elapsedTime / 60, elapsedTime % 60)
                val formattedTime = if (callTime==0L) "Ringing..." else String.format("%02d:%02d", elapsedTime / 60, elapsedTime % 60)
                collapsedViews.setTextViewText(R.id.tvTimer, formattedTime)
                expandedViews.setTextViewText(R.id.tvTimer, formattedTime)
                notificationBuilder.setContentText(formattedTime) // Update status bar text
                notificationBuilder.setCustomContentView(collapsedViews)
                notificationBuilder.setCustomBigContentView(expandedViews)
                Log.e("TAG", "Updating timer: $formattedTime")
                getSystemService(NotificationManager::class.java).notify(NOTIFICATION_ID, notificationBuilder.build())
                delay(1000) // Update every second
            }
        }
        val callState = CallState(
            isOngoing = true,
            userName = userName,
            profileUrl = profileImageUrl,
            callStartTime = callTime,
            incomingType = incoming
        )
        CallStateHolder.viewModel?.updateCallState(callState)
    }

    private fun stopForegroundService() {
        updateJob?.cancel() // Stop the timer update coroutine
        stopForeground(true) // Remove the notification from the status bar
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancel(NOTIFICATION_ID) // Ensure it’s fully removed
        CallStateHolder.viewModel?.updateCallState(CallState(isOngoing = false))
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        updateJob?.cancel() // Clean up coroutine
    }
}
