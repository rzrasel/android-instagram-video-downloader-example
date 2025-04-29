package com.rzrasel.instagramvideodownload.di

import com.rzrasel.instagramvideodownload.data.datasource.InstagramRemoteDataSource
import com.rzrasel.instagramvideodownload.data.datasource.InstagramRemoteDataSourceImpl
import com.rzrasel.instagramvideodownload.data.remote.DownloadProxyService
import com.rzrasel.instagramvideodownload.data.remote.InstagramApiService
import com.rzrasel.instagramvideodownload.data.remote.dto.InstagramGraphQLService
import com.rzrasel.instagramvideodownload.data.repository.InstagramRepositoryImpl
import com.rzrasel.instagramvideodownload.domain.repository.InstagramRepository
import com.rzrasel.instagramvideodownload.domain.usecase.DownloadVideoWithProxy
import com.rzrasel.instagramvideodownload.domain.usecase.GetInstagramVideoUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://www.instagram.com/"
    private const val TIMEOUT = 30L

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("X-IG-App-ID", "1217981644879628")
                    .header("X-FB-Friendly-Name", "PolarisPostActionLoadPostQueryQuery")
                    .header(
                        "User-Agent",
                        "Mozilla/5.0 (Linux; Android 11; SAMSUNG SM-G973U) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/14.2 Chrome/87.0.4280.141 Mobile Safari/537.36"
                    )
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideInstagramApiService(client: OkHttpClient): InstagramApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(InstagramApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideInstagramGraphQLService(client: OkHttpClient): InstagramGraphQLService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InstagramGraphQLService::class.java)
    }

    @Provides
    @Singleton
    fun provideDownloadProxyService(client: OkHttpClient): DownloadProxyService {
        return Retrofit.Builder()
            .baseUrl("https://your-proxy-server.com/") // Replace with your actual proxy server URL
            .client(client)
            .build()
            .create(DownloadProxyService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        apiService: InstagramApiService,
        graphQLService: InstagramGraphQLService,
        downloadProxyService: DownloadProxyService
    ): InstagramRemoteDataSource {
        return InstagramRemoteDataSourceImpl(apiService, graphQLService, downloadProxyService)
    }

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: InstagramRemoteDataSource): InstagramRepository {
        return InstagramRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideGetVideoUrlUseCase(repository: InstagramRepository): GetInstagramVideoUrl {
        return GetInstagramVideoUrl(repository)
    }

    @Provides
    @Singleton
    fun provideDownloadVideoWithProxyUseCase(repository: InstagramRepository): DownloadVideoWithProxy {
        return DownloadVideoWithProxy(repository)
    }
}