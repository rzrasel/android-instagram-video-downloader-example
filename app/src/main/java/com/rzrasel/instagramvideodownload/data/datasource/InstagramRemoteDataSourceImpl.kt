package com.rzrasel.instagramvideodownload.data.datasource

import com.rzrasel.instagramvideodownload.data.remote.DownloadProxyService
import com.rzrasel.instagramvideodownload.data.remote.InstagramApiService
import com.rzrasel.instagramvideodownload.data.remote.dto.IGGraphQLResponseDto
import com.rzrasel.instagramvideodownload.data.remote.dto.InstagramGraphQLService
import okhttp3.ResponseBody

class InstagramRemoteDataSourceImpl(
    private val apiService: InstagramApiService,
    private val graphQLService: InstagramGraphQLService,
    private val downloadProxyService: DownloadProxyService
) : InstagramRemoteDataSource {
    override suspend fun getPostData(shortcode: String): String {
        val response = apiService.getPostData(shortcode).execute()
        if (!response.isSuccessful) {
            throw Exception("Network error: ${response.code()}")
        }
        return response.body() ?: throw Exception("Empty response")
    }

    override suspend fun getPostGraphQL(body: String): IGGraphQLResponseDto {
        return graphQLService.getPostGraphQL(body = body)
    }

    override suspend fun downloadVideoWithProxy(fileUrl: String, filename: String): ResponseBody {
        return downloadProxyService.downloadVideo(fileUrl, filename)
    }
}