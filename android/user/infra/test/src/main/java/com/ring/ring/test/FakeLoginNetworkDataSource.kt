package com.ring.ring.test.fake

import com.ring.ring.network.LoginNetworkDataSource
import com.ring.ring.network.LoginRequest
import com.ring.ring.network.LoginResponse

class FakeLoginNetworkDataSource : LoginNetworkDataSource {
    override suspend fun login(request: LoginRequest): LoginResponse {
        return LoginResponse(1L, "fakeToken")
    }
}