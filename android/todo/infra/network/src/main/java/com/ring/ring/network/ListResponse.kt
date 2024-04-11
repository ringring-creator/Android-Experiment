package com.ring.ring.network

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ListResponse(
    val todoList: List<Todo>,
) {
    @Serializable
    data class Todo(
        val id: Long,
        val title: String,
        val description: String,
        val done: Boolean,
        val deadline: LocalDate,
        val userId: Long,
    )
}