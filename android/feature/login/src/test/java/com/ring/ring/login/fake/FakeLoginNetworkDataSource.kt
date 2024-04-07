package com.ring.ring.login.fake

import com.ring.ring.login.LoginNetworkDataSource
import com.ring.ring.login.LoginRequest
import com.ring.ring.login.LoginResponse

class FakeLoginNetworkDataSource : LoginNetworkDataSource {
    override suspend fun login(request: LoginRequest): LoginResponse {
        return LoginResponse(1L, "fakeToken")
    }
}