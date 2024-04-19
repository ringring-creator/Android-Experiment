package com.ring.ring.usecase.todo

import com.ring.ring.data.Todo
import com.ring.ring.data.repository.TodoRepository
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.BadRequestException
import com.ring.ring.exception.ForbiddenException
import com.ring.ring.exception.UnauthorizedException
import com.ring.ring.usecase.InstantSerializer
import com.ring.ring.usecase.UseCase
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

class GetTodo(
    private val repository: TodoRepository = DataModules.todoRepository,
    private val userRepository: UserRepository = DataModules.userRepository,
) : UseCase<GetTodo.Req, GetTodo.Res>() {
    override suspend fun execute(req: Req): Res {
        val userId = userRepository.loadId(req.email)
            ?: throw UnauthorizedException("Your email address is not registered")
        val todo = repository.get(req.todoId) ?: throw BadRequestException("User not found")
        if (todo.userId != userId) throw ForbiddenException("You try to access another user's Todos")
        return Res(todo = todo.toGetTodo())
    }

    @Serializable
    data class Req(
        val todoId: Long,
        val email: String,
    ) : UseCase.Req

    @Serializable
    data class Res(
        val todo: ResTodo,
    ) : UseCase.Res {
        @Serializable
        data class ResTodo(
            val id: String,
            val title: String,
            val description: String,
            val done: String,
            @Serializable(with = InstantSerializer::class)
            val deadline: Instant,
        )
    }

    private fun Todo.toGetTodo(): Res.ResTodo = Res.ResTodo(
        id = id?.toString() ?: throw IllegalStateException(),
        title = title,
        description = description,
        done = done.toString(),
        deadline = deadline,
    )
}