package com.kjk.reminderapp.presenter.reminderdetail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kjk.reminderapp.data.local.ReminderDatabase
import com.kjk.reminderapp.data.local.ReminderDatabaseDao
import com.kjk.reminderapp.data.local.ReminderEntity
import com.kjk.reminderapp.presenter.reminderalarm.AlarmFunctions

class ReminderViewModelFactory(
    private val reminderId: Long,
    private val alarmFunctions: AlarmFunctions
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReminderDetailViewModel::class.java)) {
            return ReminderDetailViewModel(reminderId, alarmFunctions) as T
        }

        throw IllegalArgumentException("Unknown viewModel class")
    }
}