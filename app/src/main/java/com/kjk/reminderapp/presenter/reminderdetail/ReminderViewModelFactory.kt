package com.kjk.reminderapp.presenter.reminderdetail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kjk.reminderapp.data.local.ReminderDatabaseDao
import com.kjk.reminderapp.data.local.ReminderEntity

class ReminderViewModelFactory(
    private val database: ReminderDatabaseDao,
    private val reminder: ReminderEntity
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReminderDetailViewModel::class.java)) {
            return ReminderDetailViewModel(database, reminder) as T
        }

        throw IllegalArgumentException("Unknown viewModel class")
    }
}