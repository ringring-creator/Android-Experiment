package com.ring.ring.user.feature.login

import com.ring.ring.network.LoginRequest
import com.ring.ring.network.LoginRequest.Credentials
import com.ring.ring.network.UserNetworkDataSource
import com.ring.ring.user.infra.local.LocalUser
import com.ring.ring.user.infra.local.UserLocalDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val networkDataSource: UserNetworkDataSource,
    private val localDataSource: UserLocalDataSource,
) {
    suspend fun login(email: String, password: String) {
        val response = networkDataSource.login(
            LoginRequest(
                credentials = Credentials(email, password)
            )
        )
        localDataSource.save(
            user = LocalUser(response.userId, email, response.token)
        )
    }
}