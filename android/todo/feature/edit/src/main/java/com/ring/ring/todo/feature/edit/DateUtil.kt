package com.ring.ring.todo.feature.edit

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateUtil {
    fun format(instant: Instant): String {
        val localDateTime = toLocalDateTime(instant)
        return toYYMMDD(localDateTime)
    }

    private fun toYYMMDD(localDateTime: LocalDateTime) = "${localDateTime.year}-${
        localDateTime.monthNumber.toString().padStart(2, '0')
    }-${localDateTime.dayOfMonth.toString().padStart(2, '0')}"

    private fun toLocalDateTime(instant: Instant): LocalDateTime {
        val timeZone = TimeZone.currentSystemDefault()
        return instant.toLocalDateTime(timeZone)
    }
}