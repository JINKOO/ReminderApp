package com.kjk.reminderapp.domain.repo

import androidx.lifecycle.Transformations
import com.kjk.reminderapp.data.local.ReminderDatabase
import com.kjk.reminderapp.data.mapper.toDomainListModel

/**
 *  Repository
 *  TODO 추후, 리펙토링 필요
 */
class ReminderRepository(
    private val database: ReminderDatabase
) {

    /**
     *   database에 저장된 모든 reminder data를 fetch
     */
    val reminders = Transformations.map(database.reminderDatabaseDao.getAllReminders()) {
        it.toDomainListModel()
    }
}