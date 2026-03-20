package com.app.hihlo.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper

object AgoraUtils {
    fun stopBackgroundMusicService(context: Context) {
        val svc = Intent(context, SoundService::class.java)
        context.stopService(svc)
    }
    fun startBackgroundMusicService(context: Context) {
        try {
            val svc = Intent(context, SoundService::class.java)
            context.startService(svc)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
     fun closeApp(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)

        // Give a small delay to move to home before killing
        Handler(Looper.getMainLooper()).postDelayed({
            android.os.Process.killProcess(android.os.Process.myPid())
        }, 300)
    }

    fun isAppOnForeground(context: Context): Boolean {
        val appPackageName = context.packageName.toString()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == appPackageName) {
                return true
            }
        }
        return false
    }
    fun isForegroundServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}