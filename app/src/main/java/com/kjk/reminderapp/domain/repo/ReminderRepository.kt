package com.kjk.reminderapp.domain.repo

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kjk.reminderapp.data.local.ReminderDatabase
import com.kjk.reminderapp.data.local.ReminderEntity
import com.kjk.reminderapp.data.mapper.toDatabaseModel
import com.kjk.reminderapp.data.mapper.toDomainListModel
import com.kjk.reminderapp.data.mapper.toDomainModel
import com.kjk.reminderapp.domain.vo.ReminderVO

/**
 *  Repository
 *  TODO 추후, 리펙토링 필요
 */
class ReminderRepository(
    application: Application
) {

    /**
     *  database instance
     */
    private val database = ReminderDatabase.getInstance(application)


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
     *   database에 저장된 모든 reminder data를 fetch
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
            Log.d(TAG, "getReminder: ${it}")
            it?.toDomainModel()
        }
    }


    /**
     *  reminderID를 가지고, ReminderEntity형으로 fetch한다.
     */
    suspend fun get(reminderId: Long): ReminderEntity? {
        Log.d(TAG, "get!!!: ${reminderId}")
        val reminder = database.reminderDatabaseDao.getR(reminderId)
        Log.d(TAG, "getReminder: ${reminder}")
        return reminder
        //return database.reminderDatabaseDao.get(reminderId)!!.toDomainModel()
    }


    /**
     * Repository instance생성
     */
    companion object {

        private const val TAG = "ReminderRepository"

        private var INSTANCE: ReminderRepository? = null

        fun initialize(application: Application) {
            if (INSTANCE == null) {
                INSTANCE = ReminderRepository(application)
            }
        }

        fun getInstance(): ReminderRepository {
            return INSTANCE ?: throw IllegalStateException("ReminderRepository must be initialized!")
        }
    }
}