package com.ring.ring.test

import com.ring.ring.network.LoginRequest
import com.ring.ring.network.LoginResponse
import com.ring.ring.network.SignUpRequest
import com.ring.ring.network.UserNetworkDataSource

class FakeUserNetworkDataSource(
    private val isSimulateError: Boolean = false,
) : UserNetworkDataSource {
    var calledSignUpParameter: Credentials? = null

    private var credentialsList: MutableList<Credentials> = mutableListOf(
        Credentials("defaultEmail", "defaultPassword")
    )

    override suspend fun login(request: LoginRequest): LoginResponse {
        if (isSimulateError) throw Exception()
        return LoginResponse(1L, "fakeToken")
    }

    override suspend fun signUp(request: SignUpRequest) {
        if (isSimulateError) throw Exception()
        val credentials = Credentials(request.credentials.email, request.credentials.password)
        credentialsList.add(credentials)
        calledSignUpParameter = credentials
    }

    data class Credentials(
        val email: String,
        val password: String,
    )
}