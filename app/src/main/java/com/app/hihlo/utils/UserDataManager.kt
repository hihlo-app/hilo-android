package com.app.hihlo.utils

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.app.hihlo.model.get_reel_comments.response.Payload
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object UserDataManager {

    fun setPause(context: Context, isPaused: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        with(prefs.edit()) {
            putBoolean("com.HHA_IsPaused", isPaused)
            apply()
        }
    }

    fun isPaused(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val value = prefs.getBoolean("com.HHA_IsPaused", false)
        return value
    }

    private const val KEY_POS = "com.HHA_Position"

    fun setPosition(context: Context, position: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        val success = prefs.edit().putInt(KEY_POS, position).commit() // synchronous
        Log.e("REEL_POS", "SharedPref Saved = $position , success = $success")
    }

    fun getPosition(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        val pos = prefs.getInt(KEY_POS, -1)
        Log.e("REEL_POS", "SharedPref Read = $pos")
        return pos
    }

    fun saveOrUpdatePayload(context: Context, newPayload: Payload) {
        val sharedPref = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()

        // 1. Get old data
        val oldJson = sharedPref.getString("HHA_comment_post_list", null)
        val oldPayload: Payload = if (oldJson != null) {
            val type = object : TypeToken<Payload>() {}.type
            gson.fromJson(oldJson, type)
        } else {
            Payload()
        }

        // 2. Convert old list to mutable map for fast update
        val commentMap = oldPayload.comments.associateBy { it.id }.toMutableMap()

        // 3. Merge new data
        for (newComment in newPayload.comments) {
            commentMap[newComment.id] = newComment
            // 👉 This line handles BOTH:
            // - Add (if id not exist)
            // - Update (if id already exist)
        }

        // 4. Convert back to list
        val mergedPayload = Payload(commentMap.values.toList())

        // 5. Save back
        sharedPref.edit()
            .putString("HHA_comment_post_list", gson.toJson(mergedPayload))
            .apply()
    }

    fun getPayload(context: Context): Payload {
        val sharedPref = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val json = sharedPref.getString("HHA_comment_post_list", null)
        return if (json != null) {
            val type = object : TypeToken<Payload>() {}.type
            Gson().fromJson(json, type)
        } else {
            Payload() // default empty
        }
    }

}