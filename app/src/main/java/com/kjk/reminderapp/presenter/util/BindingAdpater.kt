package com.kjk.reminderapp.presenter.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kjk.reminderapp.data.local.ReminderEntity
import com.kjk.reminderapp.presenter.adapter.RemindersAdapter
import java.time.format.DateTimeFormatter

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


const val TIME_FORAMT = "hh:mm a"
@BindingAdapter("timeFormat")
fun setTimeFormat(textView: TextView, settingTime: Long?) {
    settingTime?.let {
        textView.text = it.toLocalDateTime().toTimeFormat()
    }
}