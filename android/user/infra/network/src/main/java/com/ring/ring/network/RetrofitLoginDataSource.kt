package com.ring.ring.network

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitLoginDataSource @Inject constructor(
    private val networkApi: RetrofitNetworkApi,
) : LoginNetworkDataSource {
    override suspend fun login(request: LoginRequest): LoginResponse {
        return networkApi.login(request)
    }
}