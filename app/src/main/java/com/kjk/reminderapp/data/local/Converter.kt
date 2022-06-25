package com.kjk.reminderapp.data.local

import androidx.room.TypeConverter
import java.util.Date


class Converter {
    @TypeConverter
    fun fromTimeStamp(value: Long?): Date? {
        return value?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}