package com.ring.ring.network

import kotlinx.serialization.Serializable

@Serializable
data class EditRequest(
    val credentials: CredentialsModel,
)