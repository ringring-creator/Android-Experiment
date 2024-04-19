package com.ring.ring.usecase.todo

import com.ring.ring.data.Todo
import com.ring.ring.data.repository.TodoRepository
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.ForbiddenException
import com.ring.ring.exception.UnauthorizedException
import com.ring.ring.usecase.UseCase
import kotlinx.serialization.Serializable

class EditTodo(
    private val repository: TodoRepository = DataModules.todoRepository,
    private val userRepository: UserRepository = DataModules.userRepository,
) : UseCase<EditTodo.Req, EditTodo.Res>() {
    override suspend fun execute(req: Req): Res {
        val userId = userRepository.loadId(req.email)
            ?: throw UnauthorizedException("This is an unregistered email")
        if (repository.verifyTodoOwner(req.todoId, userId).not()) {
            throw ForbiddenException("You try to update another user's Todos")
        }
        repository.save(todo = req.toTodo(userId))
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
            val todo: TodoModel,
        )

        fun toTodo(userId: Long): Todo = Todo(
            id = todoId,
            title = body.todo.title,
            description = body.todo.description,
            done = body.todo.done,
            deadline = body.todo.deadline,
            userId = userId,
        )
    }

    class Res : UseCase.Res
}