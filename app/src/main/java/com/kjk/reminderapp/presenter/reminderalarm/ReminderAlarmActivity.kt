package com.kjk.reminderapp.presenter.reminderalarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kjk.reminderapp.MainActivity
import com.kjk.reminderapp.R
import com.kjk.reminderapp.data.local.ReminderDatabase
import com.kjk.reminderapp.databinding.ActivityReminderAlarmBinding
import com.kjk.reminderapp.domain.vo.ReminderVO


/**
 *  리마인더의 Alarm이 울리는 Activity
 *  여기는 Activity를 사용함.
 */
class ReminderAlarmActivity : AppCompatActivity() {


    private lateinit var binding: ActivityReminderAlarmBinding


    private lateinit var viewModelFactory: ReminderAlarmViewModelFactory


    private lateinit var viewModel: ReminderAlarmViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reminder_alarm)

        /**
         *  잠금화면일 때 알람이 울리도록 보여주도록 설정
         */
        setShowWhenLocked(true)
        setTurnScreenOn(true)

        /**
         *  service 로 부터 전달 받은 reminder
         */
        val reminder = intent.getParcelableExtra<ReminderVO>("reminder")!!
        Log.d(TAG, "onCreate: ${reminder}")


        // viewModelFactory, viewModel
        viewModelFactory = ReminderAlarmViewModelFactory(reminder.id)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ReminderAlarmViewModel::class.java)

        //
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //observe
        observe()
    }


    /**
     *  viewModel의 LiveData Observing하는 함수이다.
     */
    private fun observe() {
        viewModel.reminder.observe(this, Observer { reminder ->
            reminder?.let {
                Log.d(TAG, "observe: ${reminder}")
            }
        })

        viewModel.onDisMiss.observe(this, Observer { isDismiss ->
            if (isDismiss) {
                releaseAlarm()
                moveToMainActivity()
                viewModel.onDisMissEventDone()
            }
        })
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
     * Dissmiss버튼을 클릭하면, home 리스트 화면으로 이동한다.
     */
    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    companion object {
        private val TAG = "ReminderAlarmActivity"
    }
}