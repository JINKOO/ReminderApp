package com.kjk.reminderapp.presenter.reminderalarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kjk.reminderapp.data.local.ReminderDatabase
import java.lang.IllegalArgumentException


/**
 *  viewModel factory pattern
 */
class ReminderAlarmViewModelFactory(
    private val reminderId: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReminderAlarmViewModel::class.java)) {
            return ReminderAlarmViewModel(reminderId) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }

}