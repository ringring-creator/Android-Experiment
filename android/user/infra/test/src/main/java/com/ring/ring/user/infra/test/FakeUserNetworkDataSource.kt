package com.ring.ring.user.infra.test

import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.model.UserNetworkDataSource

class FakeUserNetworkDataSource : UserNetworkDataSource {
    private var credentialsList: MutableList<Credentials> = mutableListOf(
        Credentials("defaultEmail", "defaultPassword")
    )

    override suspend fun login(credentials: Credentials): User {
        if (credentialsList.contains(credentials).not()) throw Exception()
        return User(1L, credentials.email, credentials.password)
    }

    override suspend fun signUp(credentials: Credentials) {
        credentialsList.add(credentials)
    }
}