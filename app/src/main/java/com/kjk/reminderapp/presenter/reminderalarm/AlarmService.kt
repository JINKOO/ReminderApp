package com.kjk.reminderapp.presenter.reminderalarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.kjk.reminderapp.R
import com.kjk.reminderapp.domain.vo.ReminderVO
import java.lang.UnsupportedOperationException

/**
 *  Alarm recevier가 호출할 Service class
 */
private const val TAG = "AlarmService"
class AlarmService : Service() {

    /**
     *  reminder의 ringtone을 사용한다.
     */
    private lateinit var ringtone: Ringtone



    /**
     *  onCreate() service실행 시 가장 먼저 실행 된다.
     */
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }



    /**
     *  Activity, BroadCast Receiver와 같은 다른 컴포넌트가 service를 호출할 때,
     *  넘긴 intent는 여기서 부터 사용 가능하다.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val reminder = intent?.getParcelableExtra<ReminderVO>("reminder")
        Toast.makeText(this, "${reminder!!.title} 알람이 울립니다.", Toast.LENGTH_SHORT).show()


        Log.d(TAG, "onStartCommand: ${reminder.ringToneTitle}")
        ringtone = RingtoneManager.getRingtone(baseContext, reminder.ringTonePath.toUri())
        ringtone.play()


        val manager = baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(
            NotificationChannel(
                "alarmtitle",
                AlarmReceiver.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableVibration(true)
            }
        )

        val pendingIntent: PendingIntent =
            Intent(this, ReminderAlarmActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE)
            }

        val notification: Notification = Notification.Builder(this, "alarmtitle")
            .setContentTitle(reminder.title)
            .setContentText("알람")
            .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
            .setContentIntent(pendingIntent)
            .setTicker("아아아아아")
            .build()

        // Notification ID cannot be 0.
        startForeground(reminder.id.toInt(), notification)


        /**
         * 방어 코드
         */
//        try {
//            Log.d(TAG, "try: ")
//            Thread.sleep(5000)
//        } catch(e: Exception) {
//            Log.d(TAG, "onStartCommand: ${e.localizedMessage}")
//        }

        // 알람이 울리면, reminder alarm화면으로 이동해야 한다.
        val intentToReminderAlarm = Intent(this, ReminderAlarmActivity::class.java).apply {
            putExtra("reminder", reminder)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intentToReminderAlarm)

        return START_STICKY
    }



    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: ")
        throw UnsupportedOperationException("Not yet implemented")
    }


    /**
     *  Service 생명주기 함수 중 종료될 때,
     *  Activity나 다른 컴포넌트 에서 호출한다.
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        if (ringtone.isPlaying) {
            ringtone.stop()
        }
    }
}