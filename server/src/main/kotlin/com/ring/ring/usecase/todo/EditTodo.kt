package com.ring.ring.usecase.todo

import com.ring.ring.data.Todo
import com.ring.ring.data.repository.TodoRepository
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.NotLoggedInException
import com.ring.ring.usecase.UseCase
import kotlinx.serialization.Serializable

class EditTodo(
    private val repository: TodoRepository = DataModules.todoRepository,
    private val userRepository: UserRepository = DataModules.userRepository,
) : UseCase<EditTodo.Req, EditTodo.Res>() {
    override suspend fun execute(req: Req): Res {
        val userId = userRepository.loadId(req.email) ?: throw NotLoggedInException()
        repository.save(todo = req.todo.toTodo(userId))
        return Res()
    }

    @Serializable
    data class Req(
        val todo: Body,
        val email: String,
    ) : UseCase.Req {
        @Serializable
        data class Body(
            val todo: TodoModel,
        ) {
            fun toTodo(userId: Long): Todo = Todo(
                id = todo.id,
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