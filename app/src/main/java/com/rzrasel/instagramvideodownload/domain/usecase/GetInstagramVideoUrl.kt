package com.rzrasel.instagramvideodownload.domain.usecase

import com.rzrasel.instagramvideodownload.domain.repository.InstagramRepository
import okhttp3.ResponseBody
import javax.inject.Inject

class GetInstagramVideoUrl @Inject constructor(
    private val repository: InstagramRepository
) {
    suspend operator fun invoke(shortcode: String): String {
        return repository.fetchVideoUrl(shortcode)
    }
}