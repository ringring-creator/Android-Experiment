package com.ring.ring.login

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitLoginDataSource @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
    baseUrl: String = BuildConfig.BACKEND_URL
) : LoginNetworkDataSource {
    private val networkApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitNetworkApi::class.java)

    override suspend fun login(request: LoginRequest): LoginResponse {
        return networkApi.login(request)
    }
}