package com.ring.ring.util.date

import kotlinx.datetime.Instant

interface DateUtil {
    fun currentInstant(): Instant
    fun format(instant: Instant): String
    fun toInstant(epochMilliseconds: Long): Instant
}