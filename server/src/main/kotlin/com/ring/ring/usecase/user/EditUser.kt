package com.ring.ring.usecase.user

import com.ring.ring.data.User
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.UnauthorizedException
import com.ring.ring.usecase.UseCase
import kotlinx.serialization.Serializable

class EditUser(
    private val repository: UserRepository = DataModules.userRepository,
) : UseCase<EditUser.Req, EditUser.Res>() {
    override suspend fun execute(req: Req): Res {
        val userId = repository.loadId(req.currentEmail)
            ?: throw UnauthorizedException("This is an unregistered email")
        repository.save(
            user = req.credentials.toUser(userId)
        )
        return Res()
    }

    @Serializable
    data class Req(
        val currentEmail: String,
        val credentials: Body,
    ) : UseCase.Req {
        @Serializable
        data class Body(
            val email: String,
            val password: String,
        ) {
            fun toUser(id: Long): User = User(
                id = id,
                email = email,
                password = password,
            )
        }
    }

    class Res : UseCase.Res
}