package com.kjk.reminderapp.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "reminder_table")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    var reminderId: Long = 0L,
    var title: String,
    var settingTime: Long,
    var ringTonePath: String,
    var ringToneTitle: String,
    var isActivate: Boolean = false
) : Parcelable