package com.app.hihlo.utils.broadcast
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class CallEndBroadcast(private val callEndCallback: CallEndCallback): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("TAG", "onReceive: picture called", )
        callEndCallback.onCallEnd()
    }
    interface CallEndCallback{
        fun onCallEnd()
    }
}