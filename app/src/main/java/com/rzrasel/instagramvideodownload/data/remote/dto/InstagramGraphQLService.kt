package com.rzrasel.instagramvideodownload.data.remote.dto

import retrofit2.http.*

interface InstagramGraphQLService {
    @Headers(
        "User-Agent: Mozilla/5.0 (Linux; Android 11; SAMSUNG SM-G973U) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/14.2 Chrome/87.0.4280.141 Mobile Safari/537.36",
        "X-IG-App-ID: 1217981644879628",
        "X-FB-Friendly-Name: PolarisPostActionLoadPostQueryQuery",
        "Content-Type: application/x-www-form-urlencoded"
    )
    @POST("graphql/query")
    suspend fun getPostGraphQL(
        @Query("query_hash") queryHash: String = "8845758582119845",
        @Body body: String
    ): IGGraphQLResponseDto
}