package com.ring.ring.usecase.user

import com.ring.ring.data.User
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.BadRequestException
import com.ring.ring.usecase.UseCase
import kotlinx.serialization.Serializable

class Login(
    private val userRepository: UserRepository = DataModules.userRepository,
) : UseCase<Login.Req, Login.Res>() {
    override suspend fun execute(req: Req): Res {
        val user = req.credentials.toUser().let {
            it.copy(password = Cipher.hashWithSHA256(it.password))
        }
        val userId = userRepository.loadId(user)
            ?: throw BadRequestException(message = "Id is not found.")

        return Res(userId, generateJwtToken(user))
    }

    private fun generateJwtToken(user: User): String = JwtProvider.createJWT(user)

    @Serializable
    data class Req(
        val credentials: Credentials,
    ) : UseCase.Req {
        @Serializable
        data class Credentials(
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