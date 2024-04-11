package com.ring.ring.todo.infra.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ring.ring.todo.infra.network.BuildConfig
import com.ring.ring.todo.infra.network.RetrofitTodoNetworkApi
import com.ring.ring.todo.infra.network.TodoNetworkDataSource
import com.ring.ring.todo.infra.network.TodoRetrofitDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
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
        networkJson: Json,
        okhttpCallFactory: Call.Factory,
    ): RetrofitTodoNetworkApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BACKEND_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitTodoNetworkApi::class.java)

}