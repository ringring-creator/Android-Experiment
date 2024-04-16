package com.ring.ring.user.infra.model

data class Id(
    val value: Long
) {
    init {
        if (isInvalidId(value)) throw IllegalArgumentException("Id is 0 or more")
    }

    companion object {
        fun isInvalidId(id: Long): Boolean = id < 0
    }
}
