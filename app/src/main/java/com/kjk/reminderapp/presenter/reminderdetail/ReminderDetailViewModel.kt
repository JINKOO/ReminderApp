package com.kjk.reminderapp.presenter.reminderdetail

import android.util.Log
import androidx.lifecycle.*
import com.kjk.reminderapp.data.mapper.toDomainModel
import com.kjk.reminderapp.domain.repo.ReminderRepository
import com.kjk.reminderapp.domain.vo.ReminderVO
import com.kjk.reminderapp.presenter.util.toLocalDateTime
import com.kjk.reminderapp.presenter.util.toMilliSeconds
import kotlinx.coroutines.launch
import java.time.LocalDateTime


/**
 *  reminder detail화면에서 사용할 viewModel
 */
class ReminderDetailViewModel(
    private val reminderId: Long
) : ViewModel() {

    /**
     *  repository
     */
    private val repository = ReminderRepository.getInstance()
    

    /**
     *  reminderId로
     *  database에서 특정 reminder를 가져온다.
     */
    val reminder: LiveData<ReminderVO?> = repository.getReminder(reminderId)


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
     * 'Save'버튼 클릭 시,
     *  모든 항목 값이 입력되지 않았을 경우,
     *  toast message를 trigger하는 LiveData
     */
    private val _showInputCheckMessage = MutableLiveData<Boolean>()
    val showInputCheckMessage: LiveData<Boolean>
        get() = _showInputCheckMessage


    /**
     * 현재 reminder setting 작업이,
     * CREATE인지,
     * UPDATE인지
     * 판별하는 LiveData
     */
    lateinit var reminderSettingTaskType: ReminderSettingTaskType


    init {
        Log.d(TAG, "init: ${reminder.value}")
        setReminderSettingTaskType()
        _reminderSettingTime.value = getDefaultSettingTime()
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
     *  이때, 모든 입력값이 있는지 체크하는 로직 필요하다.
     */
    fun onSaveReminder() {
        // 입력 값 예외처리
        if (!isAllInputValid()) {
            return
        }
        when (reminderSettingTaskType) {
            ReminderSettingTaskType.CREATE -> {
                createReminder()
            }
            ReminderSettingTaskType.UPDATE -> {
                Log.d(TAG, "onSaveReminder: hehehe")
                updateReminder()
            }
        }
        _navigateToHome.value = true
    }


    /**
     *  detail 화면에서, 모든 입력값이 있는지 판별하는 함수.
     *
     *  reminder title, setting time(default는 처음 detail 화면 들어온 시간), ringtoneTitle(default는 기본 노래)
     *  따라서 여기서는 우선 title의 값 만 check하면 된다.
     */
    private fun isAllInputValid(): Boolean {
        if (_reminderTitle.value.isNullOrEmpty()) {
            Log.d(TAG, "allInputValid: ")
            _showInputCheckMessage.value = true
            return false
        }
        return true
    }

    
    /**
     *  새로운 Reminder를 추가한다.
     *  database의 insert작업이 필요하다
     */
    private fun createReminder() {
        viewModelScope.launch {
            Log.d(TAG, "createReminder: ${reminderTitle.value}")
            if (isBeforeCurrentTime()) {
                setReminderTimeToNextDay()
            }
            val newReminder = ReminderVO(
                id = reminderId,
                title = reminderTitle.value!!,
                settingTime = reminderSettingTime.value!!,
                ringTonePath = ringtonePath.value!!,
                ringToneTitle = ringtoneTitle.value!!
            )
            insert(newReminder)
        }
    }


    /**
     *  reminder update하는 로직
     */
    private fun updateReminder() {
        viewModelScope.launch {
            Log.d(TAG, "updateReminder!!: ${reminderId}")
            val currentReminder = repository.get(reminderId) ?: return@launch

            Log.d(TAG, "updateReminder: get result : ${currentReminder}")

            /**
             *  update Reminder
             *  update인데 아무 내용을 건드리리 않으면,
             *  기존의 값으로, update한다.
             */
            currentReminder.run {
                //id = reminderId
                title = reminderTitle.value ?: "dffdfdfd"
                settingTime = reminderSettingTime.value ?: settingTime
                ringTonePath = ringtonePath.value ?: ringTonePath
                ringToneTitle = ringtoneTitle.value ?: ringToneTitle
            }

            // update
            update(currentReminder.toDomainModel())
        }
        Log.d(TAG, "updateReminder: here")
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
     */
    fun setReminderTime(hourOfDay: Int, minute: Int) {
        val now = LocalDateTime.now()
        val localDateTime = LocalDateTime.of(
            now.year,
            now.monthValue,
            now.dayOfMonth,
            hourOfDay,
            minute,
            0,
            0
        )
        Log.d(TAG, "setReminderTime!!: ${localDateTime}")
        _reminderSettingTime.value = localDateTime.toMilliSeconds()
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
    private suspend fun insert(reminder: ReminderVO) {
        repository.insertReminder(reminder)
    }



    /**
     *  save button이 클릭되면,
     *  수정되거나, 새로 추가한 reminder가 dabase에 저장되어야 한다.
     */
    private suspend fun update(reminder: ReminderVO) {
        repository.updateReminder(reminder)
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


    /**
     *  toast message 보여주기 완료.
     */
    fun showInputCheckMessageDone() {
        _showInputCheckMessage.value = false
    }


    /**
     *  설정한 alarm 시간이 현재 시간보다 이전이면,
     *  다음날 동일 시간에 울리도록 한다.
     */
    private fun isBeforeCurrentTime(): Boolean {
        val settingTime = reminderSettingTime.value!!.toLocalDateTime()
        if (settingTime.isBefore(LocalDateTime.now())) {
            return true
        }
        return false
    }


    /**
     *  현재 지정된 알람 시간을
     *  현재 시간보다 전이면, 다음날 울리도록 설정한다.
     */
    private fun setReminderTimeToNextDay() {
        val settingTime = reminderSettingTime.value!!.toLocalDateTime()
        Log.d(TAG, "isBeforeCurrentTime: ${settingTime}")
        val nextDay = settingTime.plusDays(1)
        Log.d(TAG, "isBeforeCurrentTime: ${nextDay}")
        _reminderSettingTime.value = nextDay.toMilliSeconds()
    }


    /**
     *  default 리마인더 setting Time
     */
    private fun getDefaultSettingTime(): Long {
        val now = LocalDateTime.now()
        return LocalDateTime.of(
            now.year,
            now.monthValue,
            now.dayOfMonth,
            now.hour,
            now.minute,
            0,
            0
        ).toMilliSeconds()
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