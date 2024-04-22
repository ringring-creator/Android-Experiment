package com.ring.ring.todo.feature.edit.viewmodel

sealed class EditTodoEvent {
    object EditSuccess : EditTodoEvent()
    object DeleteSuccess : EditTodoEvent()
    object EditError : EditTodoEvent()
    object DeleteError : EditTodoEvent()
    object GetTodoError : EditTodoEvent()
    object UnauthorizedError : EditTodoEvent()
}