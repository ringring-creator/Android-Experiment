package com.ring.ring.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ring.ring.network.RetrofitUserNetworkApi
import com.ring.ring.network.UserRetrofitDataSource
import com.ring.ring.user.infra.model.UserNetworkDataSource
import com.ring.ring.user.infra.network.BuildConfig
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
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Modules {
    @Provides
    @Singleton
    fun providesLoginNetworkDataSource(
        networkApi: RetrofitUserNetworkApi
    ): UserNetworkDataSource = UserRetrofitDataSource(
        networkApi = networkApi
    )

    @Provides
    @Singleton
    fun providesRetrofitNetworkApi(
        @UserJson networkJson: Json,
        @UserOkHttp okhttpCallFactory: Call.Factory,
    ): RetrofitUserNetworkApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BACKEND_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitUserNetworkApi::class.java)

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UserJson

    @Provides
    @Singleton
    @UserJson
    fun providesNetworkJson() = Json {
        ignoreUnknownKeys = true
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UserOkHttp

    @Provides
    @Singleton
    @UserOkHttp
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