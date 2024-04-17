package com.ring.ring.user.infra.test

import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.model.UserNetworkDataSource

class FakeUserNetworkDataSource(
    private var credentialsList: MutableList<Credentials> = mutableListOf(),
) : UserNetworkDataSource {
    override suspend fun login(credentials: Credentials): User {
        if (credentialsList.contains(credentials).not()) throw Exception()
        return User.generate(1L, credentials.email.value, credentials.password.value)
    }

    override suspend fun signUp(credentials: Credentials) {
        credentialsList.add(credentials)
    }
}