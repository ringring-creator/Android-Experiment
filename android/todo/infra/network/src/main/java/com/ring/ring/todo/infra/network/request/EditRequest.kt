package com.ring.ring.todo.infra.network.request

import com.ring.ring.todo.infra.network.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class EditRequest(
    val id: Long,
    val title: String,
    val description: String,
    val done: Boolean,
    @Serializable(with = InstantSerializer::class)
    val deadline: Instant,
)