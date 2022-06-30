package com.kjk.reminderapp.presenter.reminderalarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kjk.reminderapp.MainActivity
import com.kjk.reminderapp.R
import com.kjk.reminderapp.domain.vo.ReminderVO


/**
 *  알람 시간이 되었을 때, 동작
 *  정해진 시간에 AlarmManager로 부터 호출된다.
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    enableVibration(true)
                }
        )

        val reminder = intent?.getParcelableExtra<ReminderVO>("reminder")
        Log.d(TAG, "onReceive: ${reminder}")


        /**
         *  알람이 울리면, Service처리한다.
         */
        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("reminder", reminder)
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "onReceive: HERE")
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }


        /**
         *  Alarm이 발생하고, notification을 탭 했을 때,
         *  PendingIntent를 설정한다.
         *  이 때, 탭 했을 때, 실행 시켜야 하는 Activity를 명시한다.
         *  알림의 응답에만, 존재하는 Activity인 경우,
         *  사용자가 app을 실행하는 동안, 이 Activity로 이동해야하는 경우는 없으므로,
         *  Activity가 기존의 back stack에 추가되는 대신에 새 작업을 시작한다.
         */
//        val intentToMain = Intent(context, MainActivity::class.java)
//            .apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }

//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            reminder!!.id.toInt(),
//            intentToMain,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//
//        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
//            .setContentTitle(reminder.title)
//            .setContentText(reminder.title)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            // 사용자가 탭하면, 자동 알림 삭제
//            .setAutoCancel(true)
//            // 사용자가 탭하면, 이동해야할 intent를 지정.
//            .setContentIntent(pendingIntent)
//            .build()
//
//        with(NotificationManagerCompat.from(context)) {
//            Log.d(TAG, "onReceive: ")
//            notify(1, notification)
//        }
    }

    companion object {
        private const val TAG = "AlarmRecevier"
        const val CHANNEL_ID = "channel"
        const val CHANNEL_NAME = "channel1"
    }
}