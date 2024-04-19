package com.ring.ring.usecase.user

import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.UnauthorizedException
import com.ring.ring.usecase.UseCase
import kotlinx.serialization.Serializable

class WithdrawalUser(
    private val repository: UserRepository = DataModules.userRepository,
) : UseCase<WithdrawalUser.Req, WithdrawalUser.Res>() {
    override suspend fun execute(req: Req): Res {
        val userId = repository.loadId(req.email)
            ?: throw UnauthorizedException("This is an unregistered email")
        repository.delete(id = userId)
        return Res()
    }

    @Serializable
    data class Req(
        val email: String
    ) : UseCase.Req

    class Res : UseCase.Res
}