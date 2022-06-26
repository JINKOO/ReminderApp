package com.kjk.reminderapp.presenter.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 *  리스트 아이템에 보여줄 setting time format
 */
private const val TIME_FORMAT = "hh:mm a"


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
    return this.format(DateTimeFormatter.ofPattern(TIME_FORMAT))
}