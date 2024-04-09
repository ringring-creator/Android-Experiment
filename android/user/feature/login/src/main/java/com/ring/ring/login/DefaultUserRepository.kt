package com.ring.ring.login

import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val networkDataSource: com.ring.ring.network.LoginNetworkDataSource,
    private val localDataSource: com.ring.ring.local.UserLocalDataSource,
) : UserRepository {
    override suspend fun login(email: String, password: String) {
        val response = networkDataSource.login(
            com.ring.ring.network.LoginRequest(
                com.ring.ring.network.LoginRequest.Account(
                    email,
                    password
                )
            )
        )
        localDataSource.save(com.ring.ring.local.User(response.userId, email, response.token))
    }
}