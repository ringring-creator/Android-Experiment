package com.ring.ring.todo.feature.list

sealed class TodoListEvent {
    object FetchErrorEvent : TodoListEvent()
    object ToggleDoneErrorEvent : TodoListEvent()
}