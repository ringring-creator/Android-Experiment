package com.ring.ring.usecase.todo

import com.ring.ring.data.Todo
import com.ring.ring.data.repository.TodoRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.BadRequestException
import com.ring.ring.usecase.UseCase
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

class EditTodo(
    private val repository: TodoRepository = DataModules.todoRepository,
) : UseCase<EditTodo.Req, EditTodo.Res>() {
    override suspend fun execute(req: Req): Res {
        repository.save(todo = req.toTodo())
        return Res()
    }

    @Serializable
    data class Req(
        val todo: ReqTodo,
    ) : UseCase.Req {
        @Serializable
        data class ReqTodo(
            val id: Long?,
            val title: String,
            val description: String,
            val done: Boolean,
            val deadline: LocalDate,
            val userId: Long,
        )

        fun toTodo(): Todo = Todo(
            id = todo.id ?: throw BadRequestException(message = "Id is invalid"),
            title = todo.title,
            description = todo.description,
            done = todo.done,
            deadline = todo.deadline,
            userId = todo.userId,
        )
    }

    class Res : UseCase.Res
}