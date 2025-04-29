package com.rzrasel.instagramvideodownload.data.datasource

import com.rzrasel.instagramvideodownload.data.remote.dto.IGGraphQLResponseDto
import okhttp3.ResponseBody

interface InstagramRemoteDataSource {
    suspend fun getPostData(shortcode: String): String
    suspend fun getPostGraphQL(body: String): IGGraphQLResponseDto
    suspend fun downloadVideoWithProxy(fileUrl: String, filename: String): ResponseBody
}