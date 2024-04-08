package com.ring.ring.login.fake

class FakeLoginNetworkDataSource : com.ring.ring.network.LoginNetworkDataSource {
    override suspend fun login(request: com.ring.ring.network.LoginRequest): com.ring.ring.network.LoginResponse {
        return com.ring.ring.network.LoginResponse(1L, "fakeToken")
    }
}