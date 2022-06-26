package com.kjk.reminderapp.presenter.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kjk.reminderapp.data.local.ReminderEntity
import com.kjk.reminderapp.presenter.adapter.RemindersAdapter

/**
 *  recyclerView에 reminder리스트 set
 */
@BindingAdapter("reminderList")
fun setReminderList(recyclerView: RecyclerView, reminders: List<ReminderEntity>?) {
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
fun setEmptyReminderTextView(textView: TextView, reminders: List<ReminderEntity>?) {
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