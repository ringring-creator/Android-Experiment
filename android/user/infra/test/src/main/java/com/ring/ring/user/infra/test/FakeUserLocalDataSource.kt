package com.ring.ring.user.infra.test

import com.ring.ring.user.infra.local.LocalUser
import com.ring.ring.user.infra.local.UserLocalDataSource
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserLocalDataSource : UserLocalDataSource {
    private var user = MutableStateFlow(LocalUser(0L, "", ""))
    override suspend fun save(user: LocalUser) {
        this.user.value = user
    }

    override suspend fun getUser() = user.value
}