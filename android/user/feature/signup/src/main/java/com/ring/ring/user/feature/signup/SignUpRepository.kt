package com.ring.ring.user.feature.signup

import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.UserNetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpRepository @Inject constructor(
    private val networkDataSource: UserNetworkDataSource,
) {
    suspend fun signUp(email: String, password: String) {
        networkDataSource.signUp(Credentials.issue(email, password))
    }
}