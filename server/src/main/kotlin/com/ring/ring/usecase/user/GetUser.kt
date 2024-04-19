package com.ring.ring.usecase.user

import com.ring.ring.data.User
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.BadRequestException
import com.ring.ring.exception.UnauthorizedException
import com.ring.ring.usecase.UseCase
import kotlinx.serialization.Serializable

class GetUser(
    private val repository: UserRepository = DataModules.userRepository,
) : UseCase<GetUser.Req, GetUser.Res>() {
    override suspend fun execute(req: Req): Res {
        val userId = repository.loadId(req.email)
            ?: throw UnauthorizedException("This is an unregistered email")
        val user = repository.get(userId) ?: throw BadRequestException("User not found")
        return Res(user = user.toReqUser())
    }

    @Serializable
    data class Req(
        val email: String
    ) : UseCase.Req

    @Serializable
    data class Res(
        val user: ReqUser
    ) : UseCase.Res {
        @Serializable
        data class ReqUser(
            val id: Long,
            val email: String,
            val password: String,
        )
    }

    private fun User.toReqUser(): Res.ReqUser {
        return Res.ReqUser(
            id = id ?: throw IllegalStateException(),
            email = email,
            password = password
        )
    }
}