package com.ring.ring.usecase.todo

import com.ring.ring.data.repository.TodoRepository
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.ForbiddenException
import com.ring.ring.exception.UnauthorizedException
import com.ring.ring.usecase.UseCase
import kotlinx.serialization.Serializable

class EditTodoDone(
    private val repository: TodoRepository = DataModules.todoRepository,
    private val userRepository: UserRepository = DataModules.userRepository,
) : UseCase<EditTodoDone.Req, EditTodoDone.Res>() {
    override suspend fun execute(req: Req): Res {
        val userId = userRepository.loadId(req.email)
            ?: throw UnauthorizedException("This is an unregistered email")
        if (repository.verifyTodoOwner(req.todoId, userId).not()) {
            throw ForbiddenException("You try to update another user's Todos")
        }
        repository.updateDone(
            id = req.todoId,
            done = req.body.done,
        )
        return Res()
    }

    @Serializable
    data class Req(
        val todoId: Long,
        val body: Body,
        val email: String,
    ) : UseCase.Req {
        @Serializable
        data class Body(
            val done: Boolean,
        )
    }

    class Res : UseCase.Res
}