package com.ring.ring.user.feature.signup

import com.ring.ring.network.SignUpRequest
import com.ring.ring.network.UserNetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpRepository @Inject constructor(
    private val networkDataSource: UserNetworkDataSource,
) {
    suspend fun signUp(email: String, password: String) {
        networkDataSource.signUp(SignUpRequest(SignUpRequest.Credentials(email, password)))
    }
}