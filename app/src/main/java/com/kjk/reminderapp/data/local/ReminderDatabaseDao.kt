package com.kjk.reminderapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReminderDatabaseDao {

    /**
     *  database table에
     *  새로운 reminder를 추가하는 함수.
     */
    @Insert
    suspend fun insert(reminderEntity: ReminderEntity)


    /**
     *  기존의 reminder를 수정 작업 후,
     *  database table에 update
     */
    @Update
    suspend fun update(reminderEntity: ReminderEntity)


    /**
     *  수정을 위해, ID값으로,
     *  특정 Reminder를 get하는 함수
     */
    @Query("SELECT * FROM reminder_table WHERE reminderId = :reminderId")
    suspend fun getReminder(reminderId: Long): ReminderEntity?


    /**
     *  database의 table에서
     *  모든 Reminder를 가져오는 함수
     */
    @Query("SELECT * FROM reminder_table ORDER BY settingTime ASC")
    fun getAllReminders(): LiveData<List<ReminderEntity>>

}