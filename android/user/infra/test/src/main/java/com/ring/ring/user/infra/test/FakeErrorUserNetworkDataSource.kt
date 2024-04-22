package com.ring.ring.user.infra.test

import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.model.UserNetworkDataSource

class FakeErrorUserNetworkDataSource : UserNetworkDataSource {
    override suspend fun edit(credentials: Credentials, token: String) {
        throw Exception()
    }

    override suspend fun withdrawal(token: String) {
        throw Exception()
    }

    override suspend fun login(credentials: Credentials): User {
        throw Exception()
    }

    override suspend fun signUp(credentials: Credentials) {
        throw Exception()
    }
}