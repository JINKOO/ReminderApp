package com.kjk.reminderapp.presenter.reminderhome

import android.app.Application
import android.util.Log
import android.widget.CheckBox
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kjk.reminderapp.data.local.ReminderDatabase
import com.kjk.reminderapp.domain.repo.ReminderRepository
import com.kjk.reminderapp.domain.vo.ReminderVO
import kotlinx.coroutines.launch

class ReminderHomeViewModel(
    application: Application
) : AndroidViewModel(application) {


    /**
     *  repository
     */
    private val reminderRepository = ReminderRepository.getInstance()
//        ReminderRepository(ReminderDatabase.getInstance(application))


    /**
     *  repository로 부터 가져온 reminders
     */
    val reminders: LiveData<List<ReminderVO>> = reminderRepository.getReminders()


    /**
     *  리스트에서 item선택 시.
     *  detail fragment로 넘길 reminder
     */
    private val _reminder = MutableLiveData<ReminderVO?>()
    val reminder: LiveData<ReminderVO?>
        get() = _reminder


    /**
     *  'reminder 추가하기' 선택 시
     *  detail fragment로 navigate 하는 event trigger
     */
    private val _toAddNewReminder = MutableLiveData<Boolean>()
    val toAddNewReminder: LiveData<Boolean>
        get() = _toAddNewReminder


    /**
     * detail fragment로 이동,
     */
    fun onAddNewReminderClick() {
        _toAddNewReminder.value = true
    }


    /**
     *  item에서 선택한 reminder
     */
    fun setClickedReminder(reminder: ReminderVO) {
        _reminder.value = reminder
    }


    /**
     *  detail fragment로 이동 완료 처리
     */
    fun navigateToDetailDone() {
        _toAddNewReminder.value = false
        _reminder.value = null
    }


    /**
     *  alarm checkBox상태에 따른 update
     */
    fun update(reminder: ReminderVO) {
        viewModelScope.launch {
            updateReminder(reminder)
        }
    }


    /**
     *   repository를 통해,
     *   알람 상태에 따른 update수행.
     */
    private suspend fun updateReminder(reminder: ReminderVO) {
        reminderRepository.updateReminder(reminder)
    }


    fun checkBoxClicked(reminder: ReminderVO, isChecked: Boolean) {
        viewModelScope.launch {
            Log.d(TAG, "checkBoxClicked: ${isChecked}")
            reminder.isActivate = isChecked
            Log.d(TAG, "checkBoxClicked: ${reminder.isActivate}")
            update(reminder)
        }
    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}