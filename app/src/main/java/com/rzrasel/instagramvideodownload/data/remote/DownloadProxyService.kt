package com.rzrasel.instagramvideodownload.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownloadProxyService {
    @GET
    @Streaming
    suspend fun downloadVideo(
        @Url fileUrl: String,
        @Query("filename") filename: String = "gram-grabberz-video.mp4"
    ): ResponseBody
}