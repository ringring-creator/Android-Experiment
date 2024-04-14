package com.ring.ring.usecase.todo

import com.ring.ring.data.Todo
import com.ring.ring.data.repository.TodoRepository
import com.ring.ring.data.repository.UserRepository
import com.ring.ring.di.DataModules
import com.ring.ring.exception.NotLoggedInException
import com.ring.ring.usecase.UseCase
import kotlinx.serialization.Serializable

class GetTodoList(
    private val repository: TodoRepository = DataModules.todoRepository,
    private val userRepository: UserRepository = DataModules.userRepository,
) : UseCase<GetTodoList.Req, GetTodoList.Res>() {
    override suspend fun execute(req: Req): Res {
        val userId = userRepository.loadId(req.email) ?: throw NotLoggedInException()
        val todoList = repository.list(userId)
        return Res(todoList = todoList.map { it.toTodoModel() })
    }

    @Serializable
    data class Req(
        val email: String,
    ) : UseCase.Req

    @Serializable
    data class Res(
        val todoList: List<TodoModel>
    ) : UseCase.Res

    private fun Todo.toTodoModel(): TodoModel = TodoModel(
        id = id ?: throw IllegalStateException(),
        title = title,
        description = description,
        done = done,
        deadline = deadline,
    )
}