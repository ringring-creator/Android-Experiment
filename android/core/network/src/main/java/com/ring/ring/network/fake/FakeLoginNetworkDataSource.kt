package com.ring.ring.network.fake

import androidx.annotation.VisibleForTesting
import com.ring.ring.network.LoginNetworkDataSource
import com.ring.ring.network.LoginResponse

@VisibleForTesting
class FakeLoginNetworkDataSource : LoginNetworkDataSource {
    override suspend fun login(request: com.ring.ring.network.LoginRequest): LoginResponse {
        return LoginResponse(1L, "fakeToken")
    }
}