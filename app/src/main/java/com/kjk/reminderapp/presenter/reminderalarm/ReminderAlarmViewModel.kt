package com.kjk.reminderapp.presenter.reminderalarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kjk.reminderapp.domain.repo.ReminderRepository
import com.kjk.reminderapp.domain.vo.ReminderVO
import kotlinx.coroutines.launch


/**
 *  viewModel파라미터를
 *  1. ReminderVO로 넘기는 경우,
 *  2. reminderId로 넘기고, 여기서 database로 부터 get하는 방식중에 어느 방식을?
 */
class ReminderAlarmViewModel(
    private val reminderId: Long
) : ViewModel() {


    /**
     *  repository
     */
    private val repository = ReminderRepository.getInstance()


    /**
     * 현재 울리고 있는 reminder
     */
    val reminder = repository.getReminder(reminderId)


    /**
     *  dismiss버튼이 클릭 된 경우, main으로 이동해야한다.
     */
    private val _onDisMiss = MutableLiveData<Boolean>()
    val onDisMiss: LiveData<Boolean>
        get() = _onDisMiss


    /**
     *  dissmiss 버튼 클릭
     *  1. checkBox해제를 위한, ReminderVO의 isActivate를 false로 update를 진행한다.
     *  2. 이후, MainActivity로 이동한다. move에 대한 로직은 Activity에서 처리한다.
     */
    fun onDismissEvent() {
        _onDisMiss.value = true

        // checkBox해제를 위한 update
        updateReminder(reminder.value!!)
    }


    /**
     *  alarm 해제를 위한 checkBox해제
     */
    private fun updateReminder(reminder: ReminderVO) {
        viewModelScope.launch {
            reminder.isActivate = false
            repository.updateReminder(reminder)
        }
    }


    /**
     *  MainActivity이동 완료
     */
    fun onDisMissEventDone() {
        _onDisMiss.value = false
    }
}