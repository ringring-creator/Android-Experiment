package com.ring.ring.todo.feature.create.viewmodel

sealed class CreateTodoEvent {
    object CreateTodoSuccess : CreateTodoEvent()
    object CreateTodoError : CreateTodoEvent()
    object UnauthorizedError : CreateTodoEvent()
}