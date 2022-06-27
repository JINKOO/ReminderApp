package com.kjk.reminderapp.presenter.reminderdetail

import android.util.Log
import androidx.lifecycle.*
import com.kjk.reminderapp.data.local.ReminderDatabaseDao
import com.kjk.reminderapp.data.local.ReminderEntity
import kotlinx.coroutines.launch
import java.util.*

class ReminderDetailViewModel(
    private val database: ReminderDatabaseDao,
    private val reminderId: Long
) : ViewModel() {


    /**
     *  reminderId로
     *  database에서 특정 reminder를 가져온다.
     */
    val reminder: LiveData<ReminderEntity?> = database.getReminder(reminderId).also {
        Log.d(TAG, "viewModel's ReminderEntity: ${reminderId}, ${it.value.toString()}")
    }


    /**
     *  reminder title
     */
    private val _reminderTitle = MutableLiveData<String>()
    val reminderTitle: LiveData<String>
        get() = _reminderTitle


    /**
     *  setting time
     */
    private val _reminderSettingTime = MutableLiveData<Long>()
    val reminderSettingTime: LiveData<Long>
        get() = _reminderSettingTime


    /**
     *  ringtone Content 경로
     */
    private val _ringtonePath = MutableLiveData<String>()
    val ringtonePath: LiveData<String>
        get() = _ringtonePath


    /**
     *  ringtone Title
     */
    private val _ringtoneTitle = MutableLiveData<String>()
    val ringtoneTitle: LiveData<String>
        get() = _ringtoneTitle


    /**
     *  navigate To HomeFragment
     */
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome


    /**
     *  navigate to ringtone 선택 Activity
     */
    private val _navigateToSystemRingtone = MutableLiveData<Boolean>()
    val navigateToSystemRingtone: LiveData<Boolean>
        get() = _navigateToSystemRingtone


    /**
     * 현재 reminder setting 작업이,
     * CREATE인지,
     * UPDATE인지
     * 판별하는 LiveData
     */
    lateinit var reminderSettingTaskType: ReminderSettingTaskType


    init {
        Log.d(TAG, "init: ")
        setReminderSettingTaskType()
    }


    /**
     *  현재 reminder를 setting하는 작업이,
     *  CREATE인지
     *  UPDATE인지
     *  setting하는 함수,
     */
    private fun setReminderSettingTaskType() {
        reminderSettingTaskType = if(reminderId == 0L) {
            ReminderSettingTaskType.CREATE
        } else {
            ReminderSettingTaskType.UPDATE
        }
    }


    /**
     *  Save 버튼 Click trigger 이벤트 함수,
     *  수정인지, 새로 생성하는 것인지 분기해야한다.
     */
    fun onSaveReminder() {
        Log.d(TAG, "onSaveReminder: ${reminderSettingTaskType}")
        when (reminderSettingTaskType) {
            ReminderSettingTaskType.CREATE -> {
                createReminder()
            }
            ReminderSettingTaskType.UPDATE -> {
                updateReminder()
            }
        }
        _navigateToHome.value = true
    }

    /**
     *  새로운 Reminder를 추가한다.
     *  database의 insert작업이 필요하다
     */
    private fun createReminder() {
        viewModelScope.launch {
            Log.d(TAG, "createReminder: ${reminderTitle.value}")

            val newReminder = ReminderEntity(
                title = reminderTitle.value!!,
                settingTime = reminderSettingTime.value ?: System.currentTimeMillis(),
                ringTonePath = ringtonePath.value!!,
                ringToneTitle = ringtoneTitle.value!!
            )
            insert(newReminder)
        }
    }


    /**
     *  reminder update하는 로직
     *
     */
    private fun updateReminder() {
        viewModelScope.launch {
            val currentReminder = database.get(reminderId) ?: return@launch

            Log.d(TAG, "updateReminder: ${currentReminder}")

            /**
             *  update Reminder
             *  update인데 아무 내용을 건드리리 않으면,
             *  기존의 값으로, update한다.
             */
            currentReminder.run {
                title = reminderTitle.value ?: title
                settingTime = reminderSettingTime.value ?: settingTime
                ringTonePath = ringtonePath.value ?: ringTonePath
                ringToneTitle = ringtoneTitle.value ?: ringToneTitle
            }

            // update
            update(currentReminder)
        }
    }


    /**
     *  reminder title set
     */
    fun setReminderTitle(title: String) {
        _reminderTitle.value = title
    }


    /**
     *  사용자가 TimePicker로 선택 한 시간을,
     *  long으로 변환한다.
     *
     *  TODO LocalTime으로 변경 할 것.
     */
    fun setReminderTime(hourOfDay: Int, minute: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        Log.d(TAG, "setRemindTime: ${cal.timeInMillis}")
        _reminderSettingTime.value = cal.timeInMillis
    }


    /**
     *  default혹은, 사용자가 선택한
     *  ringtone content를 set한다.
     */
    fun setRingtone(ringtonePath: String, ringToneTitle: String) {
        _ringtonePath.value = ringtonePath
        _ringtoneTitle.value = ringToneTitle
    }


    /**
     * database insert
     */
    private suspend fun insert(reminder: ReminderEntity) {
        database.insert(reminder)
    }



    /**
     *  save button이 클릭되면,
     *  수정되거나, 새로 추가한 reminder가 dabase에 저장되어야 한다.
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


    /**
     *  system ringtone 화면으로 이동
     */
    fun navigateToSelectRingtone() {
        _navigateToSystemRingtone.value = true
    }


    /**
     *  system ringtone 화면으로 이동 완료.
     */
    fun navigateToSelectRingtoneDone() {
        _navigateToSystemRingtone.value = false
    }


    companion object {
        private const val TAG = "DetailViewModel"
    }
}


/**
 *  수정 작업인지, 새로 생성하는 작업인지
 */
enum class ReminderSettingTaskType {
    CREATE,
    UPDATE
}