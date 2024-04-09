package com.ring.ring.test.fake

import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserLocalDataSource : com.ring.ring.local.UserLocalDataSource {
    private var user = MutableStateFlow(com.ring.ring.local.LocalUser(0L, "", ""))
    override suspend fun save(user: com.ring.ring.local.LocalUser) {
        this.user.value = user
    }

    override suspend fun getUser() = user.value
}