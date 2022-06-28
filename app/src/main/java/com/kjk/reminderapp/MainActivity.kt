package com.kjk.reminderapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kjk.reminderapp.presenter.reminderalarm.ReminderAlarmActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: ${Build.VERSION.SDK_INT}")

//        val intent = Intent(this, ReminderAlarmActivity::class.java)
//        startActivity(intent)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}