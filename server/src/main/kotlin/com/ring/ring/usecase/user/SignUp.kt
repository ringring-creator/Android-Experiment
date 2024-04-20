package com.ring.ring.usecase.user

import com.ring.ring.data.User
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.ConflictException
import com.ring.ring.usecase.UseCase
import kotlinx.serialization.Serializable

class SignUp(
    private val repository: UserRepository = DataModules.userRepository,
) : UseCase<SignUp.Req, SignUp.Res>() {
    override suspend fun execute(req: Req): Res {
        if (repository.exist(req.credentials.email)) {
            throw ConflictException("Email is already registered")
        }
        val user = req.credentials.toUser().let {
            it.copy(password = Cipher.hashWithSHA256(it.password))
        }
        repository.save(user = user)
        return Res()
    }

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

    class Res : UseCase.Res
}