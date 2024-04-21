package com.ring.ring.user.infra.test

import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.model.UserNetworkDataSource

class FakeUserNetworkDataSource(
    private var credentialsList: MutableList<Credentials> = mutableListOf(),
) : UserNetworkDataSource {
    private var user: User? = null

    override suspend fun edit(credentials: Credentials) {
        val index = credentialsList.indexOfFirst { it == credentials }
        credentialsList[index] = credentials
    }

    override suspend fun withdrawal() {
        credentialsList.removeIf { it.email == user?.email }
    }

    override suspend fun login(credentials: Credentials): User {
        if (credentialsList.contains(credentials).not()) throw Exception()
        user = User.generate(1L, "fakeToken", credentials)
        return User.generate(1L, "fakeToken", credentials)
    }

    override suspend fun signUp(credentials: Credentials) {
        credentialsList.add(credentials)
    }
}