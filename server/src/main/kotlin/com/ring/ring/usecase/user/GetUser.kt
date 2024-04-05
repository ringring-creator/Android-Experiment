package com.ring.ring.usecase.user

import com.ring.ring.data.User
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.usecase.UseCase
import kotlinx.serialization.Serializable

class GetUser(
    private val repository: UserRepository = DataModules.userRepository,
) : UseCase<GetUser.Req, GetUser.Res>() {
    override suspend fun execute(req: Req): Res {
        val user = repository.get(req.userId)
        return Res(user = user.toReqUser())
    }

    @Serializable
    data class Req(
        val userId: Long
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