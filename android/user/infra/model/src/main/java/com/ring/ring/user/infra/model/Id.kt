package com.ring.ring.user.infra.model

data class Id(
    val value: Long
) {
    companion object {
        fun fromLong(value: Long): Id {
            return Id(value)
        }
    }
}
