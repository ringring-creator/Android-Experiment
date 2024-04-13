package com.ring.ring.usecase.todo

import com.ring.ring.data.Todo
import com.ring.ring.data.repository.TodoRepository
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.NotLoggedInException
import com.ring.ring.usecase.InstantSerializer
import com.ring.ring.usecase.UseCase
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

class EditTodo(
    private val repository: TodoRepository = DataModules.todoRepository,
    private val userRepository: UserRepository = DataModules.userRepository,
) : UseCase<EditTodo.Req, EditTodo.Res>() {
    override suspend fun execute(req: Req): Res {
        val userId = userRepository.loadId(req.email) ?: throw NotLoggedInException()
        repository.save(todo = req.toTodo(userId))
        return Res()
    }

    @Serializable
    data class Req(
        val todo: ReqTodo,
        val email: String,
    ) : UseCase.Req {
        @Serializable
        data class ReqTodo(
            val id: Long,
            val title: String,
            val description: String,
            val done: Boolean,
            @Serializable(with = InstantSerializer::class)
            val deadline: Instant,
        )

        fun toTodo(userId: Long): Todo = Todo(
            id = todo.id,
            title = todo.title,
            description = todo.description,
            done = todo.done,
            deadline = todo.deadline,
            userId = userId,
        )
    }

    class Res : UseCase.Res
}