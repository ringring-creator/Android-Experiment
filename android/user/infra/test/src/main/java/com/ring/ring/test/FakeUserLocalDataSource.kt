package com.ring.ring.test

import com.ring.ring.local.LocalUser
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserLocalDataSource : com.ring.ring.local.UserLocalDataSource {
    private var user = MutableStateFlow(LocalUser(0L, "", ""))
    override suspend fun save(user: LocalUser) {
        this.user.value = user
    }

    override suspend fun getUser() = user.value
}