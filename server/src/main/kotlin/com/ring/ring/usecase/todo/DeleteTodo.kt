package com.ring.ring.usecase.todo

import com.ring.ring.data.repository.TodoRepository
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.AuthorizationException
import com.ring.ring.exception.NotLoggedInException
import com.ring.ring.usecase.UseCase
import kotlinx.serialization.Serializable

class DeleteTodo(
    private val repository: TodoRepository = DataModules.todoRepository,
    private val userRepository: UserRepository = DataModules.userRepository,
) : UseCase<DeleteTodo.Req, DeleteTodo.Res>() {
    override suspend fun execute(req: Req): Res {
        val userId = userRepository.loadId(req.email) ?: throw NotLoggedInException()
        if (repository.verifyTodoOwner(req.body.todoId, userId).not()) {
            throw AuthorizationException("")
        }
        repository.delete(req.body.todoId)
        return Res()
    }

    @Serializable
    data class Req(
        val body: Body,
        val email: String,
    ) : UseCase.Req {
        @Serializable
        data class Body(
            val todoId: Long,
        )
    }

    class Res : UseCase.Res
}