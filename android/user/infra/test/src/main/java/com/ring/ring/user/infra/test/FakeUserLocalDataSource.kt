package com.ring.ring.user.infra.test

import com.ring.ring.user.infra.local.LocalUser
import com.ring.ring.user.infra.local.UserLocalDataSource
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserLocalDataSource(
    private val isSimulateError: Boolean = false,
) : UserLocalDataSource {
    private var user = MutableStateFlow(LocalUser(0L, "", ""))
    override suspend fun save(user: LocalUser) {
        if (isSimulateError) throw Exception()
        this.user.value = user
    }

    override suspend fun getUser(): LocalUser {
        if (isSimulateError) throw Exception()
        return user.value
    }
}