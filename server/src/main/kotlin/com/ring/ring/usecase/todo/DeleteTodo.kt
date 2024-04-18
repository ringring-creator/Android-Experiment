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
        if (repository.verifyTodoOwner(req.todoId, userId).not()) {
            throw AuthorizationException("")
        }
        repository.delete(req.todoId)
        return Res()
    }

    @Serializable
    data class Req(
        val todoId: Long,
        val email: String,
    ) : UseCase.Req

    class Res : UseCase.Res
}