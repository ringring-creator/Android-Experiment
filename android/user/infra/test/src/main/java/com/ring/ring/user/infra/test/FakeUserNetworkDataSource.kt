package com.ring.ring.user.infra.test

import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.model.UserNetworkDataSource

class FakeUserNetworkDataSource(
    private var credentials: Credentials? = null,
    private var user: User? = null,
) : UserNetworkDataSource {

    override suspend fun edit(credentials: Credentials, token: String) {
        this.credentials = credentials
        user = User.generate(1L, token, credentials)
    }

    override suspend fun withdrawal(token: String) {
        if (user?.token != token) throw Exception()
        credentials = null
    }

    override suspend fun login(credentials: Credentials): User {
        if (this.credentials != credentials) throw Exception()
        user = User.generate(1L, "fakeToken", credentials)
        return User.generate(1L, "fakeToken", credentials)
    }

    override suspend fun signUp(credentials: Credentials) {
        this.credentials = credentials
    }
}