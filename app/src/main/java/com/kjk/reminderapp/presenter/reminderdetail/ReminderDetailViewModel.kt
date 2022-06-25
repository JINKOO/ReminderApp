package com.kjk.reminderapp.presenter.reminderdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReminderDetailViewModel : ViewModel() {

    /**
     *  Home Fragment로 navigate하는 event trigger
     */
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome


    /**
     *  navigate done
     */
    fun navigateToHomeDone() {
        _navigateToHome.value = true
    }
}