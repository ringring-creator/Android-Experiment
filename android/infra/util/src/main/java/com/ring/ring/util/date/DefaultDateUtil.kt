package com.ring.ring.util.date

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class DefaultDateUtil @Inject constructor() : DateUtil {
    override fun currentInstant(): Instant {
        return Clock.System.now()
    }

    override fun format(instant: Instant): String {
        val localDateTime = toLocalDateTime(instant)
        return toYYMMDD(localDateTime)
    }

    override fun toInstant(epochMilliseconds: Long): Instant {
        return Instant.fromEpochMilliseconds(epochMilliseconds)
    }

    private fun toYYMMDD(localDateTime: LocalDateTime) = "${localDateTime.year}-${
        localDateTime.monthNumber.toString().padStart(2, '0')
    }-${localDateTime.dayOfMonth.toString().padStart(2, '0')}"

    private fun toLocalDateTime(instant: Instant): LocalDateTime {
        val timeZone = TimeZone.currentSystemDefault()
        return instant.toLocalDateTime(timeZone)
    }
}