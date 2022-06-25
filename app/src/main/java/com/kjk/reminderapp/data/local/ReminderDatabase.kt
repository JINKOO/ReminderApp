package com.kjk.reminderapp.data.local

import android.content.Context
import androidx.room.*

@Database(entities = [ReminderEntity::class], version = 1)
//@TypeConverters(Converter::class)
abstract class ReminderDatabase : RoomDatabase() {

    abstract val reminderDatabaseDao: ReminderDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: ReminderDatabase? = null

        fun getInstance(context: Context): ReminderDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        ReminderDatabase::class.java,
                        "reminder_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}