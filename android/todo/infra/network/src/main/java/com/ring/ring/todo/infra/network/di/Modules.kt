package com.ring.ring.todo.infra.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.network.BuildConfig
import com.ring.ring.todo.infra.network.RetrofitTodoNetworkApi
import com.ring.ring.todo.infra.network.TodoRetrofitDataSource
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
        networkApi: RetrofitTodoNetworkApi
    ): TodoNetworkDataSource = TodoRetrofitDataSource(
        networkApi = networkApi
    )

    @Provides
    @Singleton
    fun providesRetrofitNetworkApi(
        @TodoJson networkJson: Json,
        @TodoOkHttp okhttpCallFactory: Call.Factory,
    ): RetrofitTodoNetworkApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BACKEND_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitTodoNetworkApi::class.java)

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TodoJson

    @Provides
    @Singleton
    @TodoJson
    fun providesNetworkJson() = Json {
        ignoreUnknownKeys = true
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TodoOkHttp

    @Provides
    @Singleton
    @TodoOkHttp
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