package com.kjk.reminderapp.domain.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kjk.reminderapp.data.local.ReminderDatabase
import com.kjk.reminderapp.data.mapper.toDatabaseModel
import com.kjk.reminderapp.data.mapper.toDomainListModel
import com.kjk.reminderapp.data.mapper.toDomainModel
import com.kjk.reminderapp.domain.vo.ReminderVO

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
//    val reminders = Transformations.map(database.reminderDatabaseDao.getAllReminders()) {
//        it.toDomainListModel()
//    }


    /**
     *  새로 생성한 reminder를
     *  room database에 insert
     */
    suspend fun insertReminder(reminder: ReminderVO) {
        database.reminderDatabaseDao.insert(reminder.toDatabaseModel())
    }

    /**
     *  기존의 reminder 수정.
     *  1. detail 화면에서 수정 작업을 진행 후, 'Save'버튼을 클릭한 경우.
     *  2. home 화면에서 alarm설정을 위한, checkbox동작을 수행하는 경우.
     */
    suspend fun updateReminder(reminder: ReminderVO) {
        database.reminderDatabaseDao.update(reminder.toDatabaseModel())
    }


    /**
     *
     */
    fun getReminders(): LiveData<List<ReminderVO>> {
        return Transformations.map(database.reminderDatabaseDao.getAllReminders()) {
            it.toDomainListModel()
        }
    }



    /**
     *  reminderId를 가지고 특정 reminder를 fetch한다.
     */
    fun getReminder(reminderId: Long): LiveData<ReminderVO?> {
        return Transformations.map(database.reminderDatabaseDao.getReminder(reminderId)) {
            it?.toDomainModel()
        }
    }


    /**
     *  reminderID를 가지고, ReminderEntity형으로 fetch한다.
     */
    suspend fun get(reminderId: Long): ReminderVO? {
        return database.reminderDatabaseDao.get(reminderId)?.toDomainModel()
    }
}