package com.ring.ring.network

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitUserDataSource @Inject constructor(
    private val networkApi: RetrofitUserNetworkApi,
) : UserNetworkDataSource {
    override suspend fun login(request: LoginRequest): LoginResponse {
        return networkApi.login(request)
    }

    override suspend fun signUp(request: SignUpRequest) {
        networkApi.signUp(request)
    }
}