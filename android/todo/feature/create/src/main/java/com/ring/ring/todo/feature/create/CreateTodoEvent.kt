package com.ring.ring.todo.feature.create

sealed class CreateTodoEvent {
    object CreateTodoSuccess : CreateTodoEvent()
    object CreateTodoError : CreateTodoEvent()
    object UnauthorizedError : CreateTodoEvent()
}