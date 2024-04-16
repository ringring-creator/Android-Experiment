package com.ring.ring.user.infra.test

import com.ring.ring.user.infra.model.User

class FakeUserLocalDataSource : com.ring.ring.user.infra.model.UserLocalDataSource {
    private var user = User.generate(
        id = 1L,
        email = "email@example.com",
        token = "fakeToken",
    )

    override suspend fun save(user: User) {
        this.user = user
    }

    override suspend fun getUser(): User {
        return user
    }
}