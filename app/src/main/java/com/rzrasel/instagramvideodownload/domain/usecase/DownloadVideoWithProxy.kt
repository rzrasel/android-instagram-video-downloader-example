package com.rzrasel.instagramvideodownload.domain.usecase

import com.rzrasel.instagramvideodownload.domain.repository.InstagramRepository
import okhttp3.ResponseBody
import javax.inject.Inject

class DownloadVideoWithProxy @Inject constructor(
    private val repository: InstagramRepository
) {
    suspend operator fun invoke(fileUrl: String, filename: String = "gram-grabberz-video.mp4"): ResponseBody {
        return repository.downloadVideoWithProxy(fileUrl, filename)
    }
}