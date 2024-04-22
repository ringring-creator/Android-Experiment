package com.ring.ring.user.infra.test

import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.model.UserLocalDataSource

class FakeUserLocalDataSource(
    private var user: User? = null
) : UserLocalDataSource {

    override suspend fun save(user: User) {
        this.user = user
    }

    override suspend fun getUser(): User? {
        return user
    }

    override suspend fun delete() {
        this.user = null
    }
}