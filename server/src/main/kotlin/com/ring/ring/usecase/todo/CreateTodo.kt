package com.ring.ring.usecase.todo

import com.ring.ring.data.Todo
import com.ring.ring.data.repository.TodoRepository
import com.ring.ring.di.DataModules
import com.ring.ring.usecase.UseCase
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

class CreateTodo(
    private val repository: TodoRepository = DataModules.todoRepository,
) : UseCase<CreateTodo.Req, CreateTodo.Res>() {
    override suspend fun execute(req: Req): Res {
        repository.save(todo = req.todo.toTodo())
        return Res()
    }

    @Serializable
    data class Req(
        val todo: ReqTodo,
    ) : UseCase.Req {
        @Serializable
        data class ReqTodo(
            val title: String,
            val description: String,
            val done: Boolean,
            val deadline: Instant,
            val userId: Long,
        ) {
            fun toTodo(): Todo = Todo(
                id = null,
                title = title,
                description = description,
                done = done,
                deadline = deadline,
                userId = userId,
            )
        }
    }

    class Res : UseCase.Res
}