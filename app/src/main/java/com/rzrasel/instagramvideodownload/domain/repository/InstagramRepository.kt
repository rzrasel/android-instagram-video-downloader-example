package com.rzrasel.instagramvideodownload.domain.repository

import okhttp3.ResponseBody

interface InstagramRepository {
    suspend fun fetchVideoUrl(shortcode: String): String
    suspend fun downloadVideoWithProxy(fileUrl: String, filename: String = "gram-grabberz-video.mp4"): ResponseBody
}