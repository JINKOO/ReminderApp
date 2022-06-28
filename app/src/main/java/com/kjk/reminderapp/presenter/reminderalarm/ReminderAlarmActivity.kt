package com.kjk.reminderapp.presenter.reminderalarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kjk.reminderapp.MainActivity
import com.kjk.reminderapp.R
import com.kjk.reminderapp.databinding.ActivityReminderAlarmBinding
import com.kjk.reminderapp.domain.vo.ReminderVO

class ReminderAlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reminder_alarm)

        /**
         *  service 로 부터 전달 받은 reminder
         */
        val reminder = intent.getParcelableExtra<ReminderVO>("reminder")!!

        binding.apply {
            reminderAlarmTitle.text = reminder.title
            dismissButton.setOnClickListener {
                releaseAlarm()
                moveToMainActivity()
            }
        }
    }


    /**
     *  dismiss 버튼을 클릭하면, service를 종료하고,
     *  main 리스트 화면으로 이동한다.
     */
    private fun releaseAlarm() {
        val intent = Intent(this, AlarmService::class.java)
        this.stopService(intent)
    }


    /**
     * Dissmiss버튼을 클릭하면, 리스트 화면으로 이동한다.
     */
    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
    }
}