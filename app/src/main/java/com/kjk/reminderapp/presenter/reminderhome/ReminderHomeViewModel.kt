package com.kjk.reminderapp.presenter.reminderhome

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kjk.reminderapp.data.local.ReminderDatabase
import com.kjk.reminderapp.data.local.ReminderEntity
import com.kjk.reminderapp.domain.repo.ReminderRepository

class ReminderHomeViewModel(
    application: Application
) : AndroidViewModel(application) {


    /**
     *  repository
     */
    private val reminderRepository =
        ReminderRepository(ReminderDatabase.getInstance(application))


    /**
     *  repository로 부터 가져온 reminders
     */
    val reminders: LiveData<List<ReminderEntity>> = reminderRepository.reminders


    /**
     *  리스트에서 item선택 시.
     *  detail fragment로 넘길 reminder
     */
    private val _reminder = MutableLiveData<ReminderEntity?>()
    val reminder: LiveData<ReminderEntity?>
        get() = _reminder


    /**
     *  'reminder 추가하기' 선택 시
     *  detail fragment로 navigate 하는 event trigger
     */
    private val _toAddNewReminder = MutableLiveData<Boolean>()
    val toAddNewReminder: LiveData<Boolean>
        get() = _toAddNewReminder


    /**
     * add layout click 한 경우
     * 새로운 Reminder 객체를 생성하고,
     * database에 insert 해야 한다. 그리고, 이동
     */
    fun onAddNewReminderClick() {
        _toAddNewReminder.value = true
    }


    /**
     *  item에서 선택한 reminder
     */
    fun setClickedReminder(reminderEntity: ReminderEntity) {
        _reminder.value = reminderEntity
    }


    /**
     *  detail fragment로 이동 완료 처리
     */
    fun navigateToDetailDone() {
        _toAddNewReminder.value = false
        _reminder.value = null
    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}