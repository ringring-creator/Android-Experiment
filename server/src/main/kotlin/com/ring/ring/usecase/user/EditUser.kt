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
        val user = req.body.toUser(userId).let {
            it.copy(password = Cipher.hashWithSHA256(it.password))
        }

        repository.save(user = user)
        return Res()
    }

    @Serializable
    data class Req(
        val currentEmail: String,
        val body: Body,
    ) : UseCase.Req {
        @Serializable
        data class Body(
            val credentials: Credentials,
        ) {
            fun toUser(id: Long): User = User(
                id = id,
                email = credentials.email,
                password = credentials.password,
            )
        }
    }

    class Res : UseCase.Res
}