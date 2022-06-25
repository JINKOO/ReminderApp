package com.kjk.reminderapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_table")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val reminderId: Long = 0L,
    val title: String,
    val settingTime: Long,
    val ringBellTitle: String,
    val isActivate: Boolean = false
)