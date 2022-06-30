package com.kjk.reminderapp.presenter.util

import android.content.Context
import android.widget.Toast
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 *  리스트 아이템에 보여줄 setting time format
 */
private const val TIME_FORMAT = "hh:mm a"
private const val TIME_FORMAT_24HOUR = "HH:mm"


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
 *  timepicke에서 선택한 hourOfDay, minute을
 *  LocalDateTime변환 후,
 *  다시 Long 타입으로 변환
 */
fun LocalDateTime.toMilliSeconds(): Long {
//    val now = LocalDateTime.now()
//    val localDateTime = LocalDateTime.of(
//        now.year,
//        now.monthValue,
//        now.dayOfMonth,
//        hourOfDay,
//        minute,
//        0
//    )
    return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}


/**
 *  LocalDateTime -> Formatted time string
 */
fun LocalDateTime.toTimeFormat(): String {
    return this.format(DateTimeFormatter.ofPattern(TIME_FORMAT))
}

fun LocalDateTime.toTimeFormat24Hour(): String {
    return this.format(DateTimeFormatter.ofPattern(TIME_FORMAT_24HOUR))
}
