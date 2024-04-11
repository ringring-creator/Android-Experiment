package com.ring.ring.todo.infra.network

import kotlinx.serialization.Serializable

@Serializable
data class ListRequest(val userId: Long)
