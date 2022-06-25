package com.kjk.reminderapp.presenter.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


private const val TIME_FORMAT = ""

/**
 *  Long -> LocalDateTime
 */
fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        TimeZone.getDefault().toZoneId()
    )
}



/**
 *  LocalDateTime -> Formatted time string
 */
fun LocalDateTime.toTimeFormat(): String {
    return this.format(DateTimeFormatter.ofPattern(TIME_FORAMT))
}