package com.ring.ring.user.feature.mypage

import com.ring.ring.network.exception.UnauthorizedException
import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.user.infra.model.UserNetworkDataSource
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val networkDataSource: UserNetworkDataSource,
    private val localDataSource: UserLocalDataSource,
) {
    suspend fun getUser(): User? {
        return localDataSource.getUser()
    }

    suspend fun edit(email: String, password: String) {
        val user = localDataSource.getUser()
            ?: throw UnauthorizedException("User information is not stored locally.")
        networkDataSource.edit(
            credentials = Credentials.issue(
                email,
                password,
            ),
            user.token
        )
    }

    suspend fun withdrawal() {
        val user = localDataSource.getUser()
            ?: throw UnauthorizedException("User information is not stored locally.")
        networkDataSource.withdrawal(user.token)
    }

    suspend fun logout() {
        localDataSource.delete()
    }
}