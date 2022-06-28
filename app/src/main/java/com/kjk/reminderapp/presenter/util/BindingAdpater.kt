package com.kjk.reminderapp.presenter.util

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kjk.reminderapp.data.local.ReminderEntity
import com.kjk.reminderapp.domain.vo.ReminderVO
import com.kjk.reminderapp.presenter.adapter.RemindersAdapter

/**
 *  recyclerView에 reminder리스트 set
 */
@BindingAdapter("reminderList")
fun setReminderList(recyclerView: RecyclerView, reminders: List<ReminderVO>?) {
    val adapter = recyclerView.adapter as RemindersAdapter
    reminders?.let {
        adapter.updateAll(reminders)
    }
}


/**
 *  emptyList인 경우,
 *  리마인더가 없다는 문구 표시
 */
@BindingAdapter("emptyReminderText")
fun setEmptyReminderTextView(textView: TextView, reminders: List<ReminderVO>?) {
    textView.visibility = if (reminders.isNullOrEmpty()) {
        View.VISIBLE
    } else {
        View.GONE
    }
}


/**
 *  Long타입의 millisecond를
 *  확장 함수를 이용해,
 *  요구 사항의 시간 format으로 변경해 set하는 함수
 */
@BindingAdapter("timeFormat")
fun setTimeFormat(textView: TextView, settingTime: Long?) {
    settingTime?.let {
        textView.text = it.toLocalDateTime().toTimeFormat()
    }
}


/**
 *  Reminder Title을 set하는 부분
 */
@BindingAdapter("editTextValue")
fun setReminderTitle(editText: EditText, reminderEntity: ReminderEntity?) {
    Log.d(TAG, "setReminderTitle: ")
    reminderEntity?.let {
        Log.d(TAG, "setReminderTitle: ${reminderEntity}")
        editText.run {
            setText(reminderEntity.title)
        }
    }
}


/**
 *  timepicker의 처음 setting된 시간 설정.
 *  CREATE인 경우에는 처음으로, Detail 화면에 들어온 시각,
 *  UPDATE인 경우에는 기존에 저장되어 있던 시간으로 보여주어야 한다.
 */
@BindingAdapter("timePickerValue")
fun setTimePicker(timePicker: TimePicker, reminderEntity: ReminderEntity?) {
    Log.d(TAG, "setTimePicker: ")
    reminderEntity?.let {
        Log.d(TAG, "setTimePicker: ${reminderEntity.settingTime}")
        timePicker.apply {
            val localDateTime = reminderEntity.settingTime.toLocalDateTime()
            hour = localDateTime.hour
            minute = localDateTime.minute
        }
    }
}


/**
 *  reminder 설정 화면에서
 *  사용자가 선택한 ringtone을 보여준다.
 */
@BindingAdapter("ringtoneTitleValue")
fun setRingtoneTitleFromUser(textView: TextView, ringtoneTitle: String?) {
    Log.d(TAG, "setRingtoneTitle: ")
    ringtoneTitle?.let {
        Log.d(TAG, "setRingtoneTitle: ${ringtoneTitle}")
        textView.text = ringtoneTitle
    }
}


/**
 * reminder가 null이 아닌 경우,
 * 기존의 reminder entity에 저장되어 있는
 * ringtone을 설정한다.
 *
 * null인경우에는, default로 설정된다.
 * 즉, 이는 Update가 아닌, Create 상태 라는 의미이다.
 */
@BindingAdapter("reminderValue")
fun setRingtoneTitleFromEntity(textView: TextView, reminderEntity: ReminderEntity?) {
    reminderEntity?.let {
        Log.d(TAG, "setRingtoneTitleFromEntity: ${reminderEntity}")
        textView.text = reminderEntity.ringToneTitle
    }
}

@BindingAdapter("checkBox")
fun setCheckBox(checkBox: CheckBox, reminderEntity: ReminderEntity?) {
    reminderEntity?.let {
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d(TAG, "setCheckBox: ${isChecked}")
        }
    }
}


private const val TAG = "BindingAdapter"