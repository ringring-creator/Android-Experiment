package com.ring.ring.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ring.ring.network.BuildConfig
import com.ring.ring.network.LoginNetworkDataSource
import com.ring.ring.network.RetrofitLoginDataSource
import com.ring.ring.network.RetrofitNetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Modules {
    @Provides
    @Singleton
    fun providesLoginNetworkDataSource(
        networkApi: RetrofitNetworkApi
    ): LoginNetworkDataSource = RetrofitLoginDataSource(
        networkApi = networkApi
    )

    @Provides
    @Singleton
    fun providesNetworkJson() = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesRetrofitNetworkApi(
        networkJson: Json,
        okhttpCallFactory: Call.Factory,
    ): RetrofitNetworkApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BACKEND_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitNetworkApi::class.java)


    @Provides
    @Singleton
    fun providesOkHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        )
        .build()
}