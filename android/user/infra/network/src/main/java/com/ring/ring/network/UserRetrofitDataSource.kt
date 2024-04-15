package com.ring.ring.network

import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.model.UserNetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRetrofitDataSource @Inject constructor(
    private val networkApi: RetrofitUserNetworkApi,
) : UserNetworkDataSource {
    override suspend fun login(credentials: Credentials): User {
        return networkApi.login(
            request = LoginRequest(
                credentials = CredentialsModel(
                    credentials.email, credentials.password
                )
            )
        ).toUser(credentials.email)
    }

    override suspend fun signUp(credentials: Credentials) {
        networkApi.signUp(
            request = SignUpRequest(
                CredentialsModel(credentials.email, credentials.password)
            )
        )
    }
}