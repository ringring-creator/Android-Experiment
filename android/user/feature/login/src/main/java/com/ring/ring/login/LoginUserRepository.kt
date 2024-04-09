package com.ring.ring.login

import com.ring.ring.local.LocalUser
import com.ring.ring.local.UserLocalDataSource
import com.ring.ring.network.LoginRequest
import com.ring.ring.network.LoginRequest.Credentials
import com.ring.ring.network.UserNetworkDataSource
import javax.inject.Inject

class LoginUserRepository @Inject constructor(
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