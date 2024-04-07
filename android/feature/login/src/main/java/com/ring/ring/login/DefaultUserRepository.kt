package com.ring.ring.login

import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val networkDataSource: LoginNetworkDataSource,
    private val localDataSource: UserLocalDataSource,
) : UserRepository {
    override suspend fun login(email: String, password: String) {
        val response = networkDataSource.login(LoginRequest(email, password))
        localDataSource.save(User(response.userId, email, response.token))
    }
}