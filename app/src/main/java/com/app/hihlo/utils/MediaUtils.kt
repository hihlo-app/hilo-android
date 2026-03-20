package com.app.hihlo.utils

import android.app.Activity
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File

object MediaUtils {
     fun uriToFile(uri: Uri, activity:Activity): File {
        val contentResolver = activity.contentResolver
        val fileExtension = getFileExtension(uri, activity)
        val fileName = "image_${System.currentTimeMillis()}.$fileExtension"
        val file = File(activity.cacheDir, fileName)

        contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return file
    }

     fun getFileExtension(uri: Uri, activity:Activity): String? {
        val contentResolver = activity.contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(contentResolver.getType(uri))
    }
     fun getVideoDuration(context: Context, uri: Uri): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri)
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            time?.toLongOrNull() ?: 0L
        } catch (e: Exception) {
            0L
        } finally {
            retriever.release()
        }
    }
}