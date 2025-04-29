package com.rzrasel.instagramvideodownload.presentation.viewmodel

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rzrasel.instagramvideodownload.domain.usecase.DownloadVideoWithProxy
import com.rzrasel.instagramvideodownload.domain.usecase.GetInstagramVideoUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InstagramViewModel @Inject constructor(
    private val getVideoUrl: GetInstagramVideoUrl,
    private val downloadVideoWithProxy: DownloadVideoWithProxy
) : ViewModel() {

    val url = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun onUrlChange(newUrl: String) {
        url.value = newUrl
        errorMessage.value = null
    }

    fun downloadVideo(context: Context) {
        val shortcode = extractShortcode(url.value) ?: run {
            errorMessage.value = "Invalid Instagram URL"
            Toast.makeText(context, "Invalid Instagram URL", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val videoUrl = getVideoUrl(shortcode)
                downloadFromUrl(context, videoUrl)
                Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                errorMessage.value = when {
                    e.message?.contains("not found", ignoreCase = true) == true -> "Post not found"
                    e.message?.contains("not a video", ignoreCase = true) == true -> "This post doesn't contain a video"
                    e.message?.contains("no video URL", ignoreCase = true) == true -> "Couldn't find video URL"
                    else -> "Error: ${e.message ?: "Unknown error"}"
                }
                Toast.makeText(context, errorMessage.value, Toast.LENGTH_LONG).show()
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun extractShortcode(url: String): String? {
        val regex = Regex("instagram\\.com\\/(p|reel|tv)\\/([^/?#]+)")
        return regex.find(url)?.groupValues?.get(2)
    }

    private fun downloadFromUrl(context: Context, url: String) {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val fileName = "instagram_video_${dateFormat.format(Date())}.mp4"

        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle("Instagram Video Download")
            setDescription("Downloading video from Instagram")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }

        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    fun downloadVideoWithProxy(context: Context, fileUrl: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val responseBody = downloadVideoWithProxy(fileUrl)
                saveVideoFromResponse(context, responseBody)
                Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
                Toast.makeText(context, errorMessage.value, Toast.LENGTH_LONG).show()
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun saveVideoFromResponse(context: Context, responseBody: ResponseBody) {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val fileName = "instagram_video_${dateFormat.format(Date())}.mp4"
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)

        try {
            responseBody.byteStream().use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            throw Exception("Failed to save video: ${e.message}")
        }
    }
}