package com.kjk.reminderapp.presenter.reminderalarm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kjk.reminderapp.domain.vo.ReminderVO

class ReminderAlarmViewModel : ViewModel() {


    /**
     *  현재 울리고 있는 reminder
     */
    private val _reminder = MutableLiveData<ReminderVO>()


    /**
     *  dissmiss 버튼 클릭
     */
    fun onDissmissEvent() {

    }
}