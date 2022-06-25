package com.kjk.reminderapp.presenter.reminderhome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kjk.reminderapp.data.local.ReminderEntity

class ReminderHomeViewModel : ViewModel() {

    /**
     *  reminder
     */
    private val _reminder = MutableLiveData<ReminderEntity?>()


    /**
     *  detail fragment로 navigate 하는 event trigger
     */
    private val _navigateToDetail = MutableLiveData<Boolean>()
    val navigateToDetail: LiveData<Boolean>
        get() = _navigateToDetail


    /**
     * add layout click 한 경우
     * 새로운 Reminder 객체를 생성하고,
     * database에 insert 해야 한다. 그리고, 이동
     */
    fun onAddNewReminderClick() {
        Log.d(TAG, "onAddLayoutClick: ")
        // TODO 현재 임시 data
        _navigateToDetail.value = true
    }

    /**
     *  detail fragment로 이동 완료 처리
     */
    fun navigateToDetailDone() {
        _navigateToDetail.value = false
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}