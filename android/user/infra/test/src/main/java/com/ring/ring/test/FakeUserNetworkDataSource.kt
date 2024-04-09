package com.ring.ring.test

import com.ring.ring.network.LoginRequest
import com.ring.ring.network.LoginResponse
import com.ring.ring.network.SignUpRequest
import com.ring.ring.network.UserNetworkDataSource

class FakeUserNetworkDataSource : UserNetworkDataSource {
    private var credentialsList: MutableList<Credentials> = mutableListOf(
        Credentials("defaultEmail", "defaultPassword")
    )

    override suspend fun login(request: LoginRequest): LoginResponse {
        return LoginResponse(1L, "fakeToken")
    }

    var calledSignUpParameter: Credentials? = null
    override suspend fun signUp(request: SignUpRequest) {
        val credentials = Credentials(request.credentials.email, request.credentials.password)
        credentialsList.add(credentials)
        calledSignUpParameter = credentials
    }

    data class Credentials(
        val email: String,
        val password: String,
    )
}