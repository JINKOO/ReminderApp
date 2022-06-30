package com.kjk.reminderapp.presenter.reminderalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.kjk.reminderapp.domain.vo.ReminderVO
import com.kjk.reminderapp.presenter.util.toLocalDateTime
import java.time.LocalDateTime

/**
 *  알람 생성 및 취소를 담당한다.
 *
 */
class AlarmFunctions(private val context: Context) {

    /**
     *  Alarm 요청 코드와
     *  내용을 AlarmReceiver에게 보낸다.
     */
    fun callAlarm(reminder: ReminderVO) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // receiver로 전달될 인텐트 설정
        val receiverIntent = Intent(context, AlarmReceiver::class.java)

        Log.d(TAG, "callAlarm: ${reminder} ")
        receiverIntent.putExtra("reminder", reminder)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id.toInt(),
            receiverIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            reminder.settingTime,
            pendingIntent
        )
    }


    /**
     *  alarm을 취소한다.
     */
    fun cancelAlarm(alarmId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )

        alarmManager.cancel(pendingIntent)
    }


    companion object {
        private const val TAG = "AlarmFunctions"
    }
}