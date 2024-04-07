package com.ring.ring.login.fake

import com.ring.ring.login.User
import com.ring.ring.login.UserLocalDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeUserLocalDataSource : UserLocalDataSource {
    private var user = MutableStateFlow(User(0L, "", ""))
    override suspend fun save(user: User) {
        this.user.value = user
    }

    override suspend fun getUser() = user.asStateFlow()
}