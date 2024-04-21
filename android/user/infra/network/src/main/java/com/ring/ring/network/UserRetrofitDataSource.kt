package com.ring.ring.network

import com.ring.ring.network.exception.ConflictException
import com.ring.ring.network.exception.UnauthorizedException
import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.model.UserNetworkDataSource
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRetrofitDataSource @Inject constructor(
    private val networkApi: RetrofitUserNetworkApi,
) : UserNetworkDataSource {
    override suspend fun edit(credentials: Credentials) {
        return try {
            networkApi.edit(
                request = EditRequest(
                    credentials = CredentialsModel(
                        credentials.email.value,
                        credentials.password.value
                    )
                )
            )
        } catch (e: Throwable) {
            throwUnauthorizedExceptionIfNeeded(e)
            throw e
        }
    }

    override suspend fun withdrawal() {
        return try {
            networkApi.withdrawal()
        } catch (e: Throwable) {
            throwUnauthorizedExceptionIfNeeded(e)
            throw e
        }
    }

    override suspend fun login(credentials: Credentials): User {
        return networkApi.login(
            request = LoginRequest(
                credentials = CredentialsModel(
                    credentials.email.value, credentials.password.value
                )
            )
        ).toUser(credentials)
    }

    override suspend fun signUp(credentials: Credentials) {
        try {
            networkApi.signUp(
                request = SignUpRequest(
                    CredentialsModel(credentials.email.value, credentials.password.value)
                )
            )
        } catch (e: Throwable) {
            throwConflictExceptionIfNeeded(e)
            throw e
        }
    }

    private fun throwConflictExceptionIfNeeded(e: Throwable) {
        if (e is HttpException && e.code() == HttpURLConnection.HTTP_CONFLICT) {
            throw ConflictException()
        }
    }

    private fun throwUnauthorizedExceptionIfNeeded(e: Throwable) {
        if (e is HttpException && e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw UnauthorizedException()
        }
    }
}