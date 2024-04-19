package com.ring.ring.usecase.todo

import com.ring.ring.data.Todo
import com.ring.ring.data.repository.TodoRepository
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.UnauthorizedException
import com.ring.ring.usecase.UseCase
import kotlinx.serialization.Serializable

class CreateTodo(
    private val todoRepository: TodoRepository = DataModules.todoRepository,
    private val userRepository: UserRepository = DataModules.userRepository,
) : UseCase<CreateTodo.Req, CreateTodo.Res>() {
    override suspend fun execute(req: Req): Res {
        val userId = userRepository.loadId(req.email)
            ?: throw UnauthorizedException("This is an unregistered email")
        todoRepository.save(todo = req.todoReq.toTodo(userId))
        return Res()
    }

    @Serializable
    data class Req(
        val todoReq: Body,
        val email: String,
    ) : UseCase.Req {
        @Serializable
        data class Body(
            val todo: TodoModel,
        ) {
            fun toTodo(userId: Long): Todo = Todo(
                id = null,
                title = todo.title,
                description = todo.description,
                done = todo.done,
                deadline = todo.deadline,
                userId = userId,
            )
        }
    }

    class Res : UseCase.Res
}