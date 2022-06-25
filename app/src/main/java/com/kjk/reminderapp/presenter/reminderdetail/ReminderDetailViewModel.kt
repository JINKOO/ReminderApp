package com.kjk.reminderapp.presenter.reminderdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kjk.reminderapp.data.local.ReminderDatabase
import com.kjk.reminderapp.data.local.ReminderDatabaseDao
import com.kjk.reminderapp.data.local.ReminderEntity
import kotlinx.coroutines.launch
import java.util.*

class ReminderDetailViewModel(
    private val database: ReminderDatabaseDao,
    reminder: ReminderEntity
) : ViewModel() {


    /**
     *  추가 혹은 update할 reminder LiveData
     */
    private val newReminder = MutableLiveData<ReminderEntity>()


    /**
     *  수정을 위해 list에서 넘어온 reminder
     */
    private val _reminder = MutableLiveData<ReminderEntity>()
    val reminder: LiveData<ReminderEntity>
        get() = _reminder


    /**
     *  two - way binding을 위한, reminder title
     */
    val reminderTitle = MutableLiveData<String>()


    /**
     *  setting time
     */
    private val _reminderSettingTime = MutableLiveData<Long>()
    val reminderSettingTime: LiveData<Long>
        get() = _reminderSettingTime


    /**
     *  navigate To HomeFragment
     */
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome


    init {
        // 수정인 경우,
        // 기존 layout에 setting한다.
        _reminder.value = reminder
    }


    /**
     *  새로운 Reminder를 추가한다.
     */
    fun addNewReminder() {
        viewModelScope.launch {
            Log.d(TAG, "addNewReminder: ${reminderTitle.value}")
            val reminder = ReminderEntity(
                title = reminderTitle.value!!,
                settingTime = reminderSettingTime.value!!,
                ringBellTitle = "HomeComing",
            )
            insert(reminder)
            _navigateToHome.value = true
        }
    }


    /**
     *  사용자가 TimePicker로 선택 한 시간을,
     *  long으로 변환한다.
     */
    fun setRemindTime(hourOfDay: Int, minute: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        Log.d(TAG, "setRemindTime: ${cal.timeInMillis}")
        _reminderSettingTime.value = cal.timeInMillis
    }


    /**
     *  save button이 클릭되면,
     *  수정되거나, 새로 추가한 reminder가 dabase에 저장되어야 한다.
     */
    fun updateReminder() {

    }


    /**
     * database insert
     */
    private suspend fun insert(reminder: ReminderEntity) {
        database.insert(reminder)
    }


    /**
     *  database update reminder
     */
    private suspend fun update(reminder: ReminderEntity) {
        database.update(reminder)
    }



    /**
     *  save button click done
     */
    fun onSaveClickEventDone() {
       _navigateToHome.value = false
    }


    companion object {
        private const val TAG = "DetailViewModel"
    }
}


/**
 *  수정 작업 인지, 새로 생성하는 작업인지
 */
enum class ReminderTask{
    CREATE,
    UPDATE
}