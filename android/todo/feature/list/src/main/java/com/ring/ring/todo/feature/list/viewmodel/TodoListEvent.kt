package com.ring.ring.todo.feature.list.viewmodel

sealed class TodoListEvent {
    object FetchErrorEvent : TodoListEvent()
    object ToggleDoneErrorEvent : TodoListEvent()
}