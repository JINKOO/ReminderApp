package com.kjk.reminderapp

import android.app.Application
import com.kjk.reminderapp.domain.repo.ReminderRepository

class ReminderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ReminderRepository.initialize(this)
    }
}