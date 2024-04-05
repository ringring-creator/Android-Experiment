package com.ring.ring.usecase.user

import com.ring.ring.data.User
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.LoginFailureException
import com.ring.ring.usecase.UseCase
import kotlinx.serialization.Serializable

class Login(
    private val userRepository: UserRepository = DataModules.userRepository,
) : UseCase<Login.Req, Login.Res>() {
    override suspend fun execute(req: Req): Res {
        val user = req.user.toUser().let {
            it.copy(password = Cipher.hashWithSHA256(it.password))
        }
        val userId = userRepository.loadId(user)
            ?: throw LoginFailureException(message = "Id is not found.")

        return Res(userId, generateJwtToken(user))
    }

    private fun generateJwtToken(user: User): String {
        return JwtProvider.createJWT(user)
            ?: throw LoginFailureException(message = "Cannot generate JWT Token")
    }

    @Serializable
    data class Req(
        val user: ReqUser,
    ) : UseCase.Req {
        @Serializable
        data class ReqUser(
            val email: String,
            val password: String,
        ) {
            fun toUser(): User = User(
                id = null,
                email = email,
                password = password,
            )
        }
    }

    @Serializable
    data class Res(
        val userId: Long,
        val token: String,
    ) : UseCase.Res
}