package com.ring.ring.usecase.todo

import com.ring.ring.data.Todo
import com.ring.ring.data.repository.TodoRepository
import com.ring.ring.di.DataModules
import com.ring.ring.usecase.UseCase
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

class GetTodoList(
    private val repository: TodoRepository = DataModules.todoRepository,
) : UseCase<GetTodoList.Req, GetTodoList.Res>() {
    override suspend fun execute(req: Req): Res {
        val todoList = repository.list(req.userId)
        return Res(todoList = todoList.map { it.toGetTodoListItem() })
    }

    @Serializable
    data class Req(
        val userId: Long,
    ) : UseCase.Req

    @Serializable
    data class Res(
        val todoList: List<ResTodo>
    ) : UseCase.Res {
        @Serializable
        data class ResTodo(
            val id: Long,
            val title: String,
            val description: String,
            val done: Boolean,
            val deadline: Instant,
            val userId: Long,
        )
    }

    private fun Todo.toGetTodoListItem(): Res.ResTodo = Res.ResTodo(
        id = id ?: throw IllegalStateException(),
        title = title,
        description = description,
        done = done,
        deadline = deadline,
        userId = userId,
    )
}