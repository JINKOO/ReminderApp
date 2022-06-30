package com.kjk.reminderapp.presenter.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import com.kjk.reminderapp.BuildConfig


/**
 *  reminder의 ringtone 지정을 위해 android 시스템이 제공하는 기본 벨소리 화면으로
 *  이동 및 선택 값 가져오는 Contract
 *
 *  Detail Fragement -> Ringtone system Activity -> DetailFragment(with, 사용자가 선택한 ringtone Uri)
 */
class SelectRingtoneContract : ActivityResultContract<Int, Uri?>() {
    override fun createIntent(context: Context, ringtoneType: Int?): Intent {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, ringtoneType)
        }
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
    }
}