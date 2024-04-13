package com.ring.ring.todo.infra.network.response

import com.ring.ring.todo.infra.network.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class GetResponse(
    val id: Long,
    val title: String,
    val description: String,
    val done: Boolean,
    @Serializable(with = InstantSerializer::class)
    val deadline: Instant,
)