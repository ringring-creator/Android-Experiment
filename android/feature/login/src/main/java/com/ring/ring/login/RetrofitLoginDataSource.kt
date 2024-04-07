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
) : LoginNetworkDataSource {
    private val BASE_URL: String = BuildConfig.BACKEND_URL
    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
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