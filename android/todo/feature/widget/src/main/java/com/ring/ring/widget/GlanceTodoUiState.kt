package com.ring.ring.widget

import kotlinx.serialization.Serializable

@Serializable
data class GlanceTodoUiState(
    val todoList: List<Todo>
) {
    @Serializable
    data class Todo(
        val id: Long,
        val title: String,
        val done: Boolean,
        val deadline: String,
    )
}