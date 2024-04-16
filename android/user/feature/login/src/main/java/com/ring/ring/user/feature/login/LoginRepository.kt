package com.ring.ring.user.feature.login

import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.user.infra.model.UserNetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val networkDataSource: UserNetworkDataSource,
    private val localDataSource: UserLocalDataSource,
) {
    suspend fun login(email: String, password: String) {
        val user = networkDataSource.login(Credentials.issue(email, password))
        localDataSource.save(user = user)
    }
}