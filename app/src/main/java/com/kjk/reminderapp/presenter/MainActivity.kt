package com.kjk.reminderapp.presenter

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.kjk.reminderapp.BuildConfig
import com.kjk.reminderapp.R
import com.kjk.reminderapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        Log.d(TAG, "onCreate: ${Build.VERSION.SDK_INT}")

        if (!Settings.canDrawOverlays(this)) {
            Log.d(TAG, "onCreate: 권한 허용안됨")
            showPermissionDialog()
        }

        initLayout()
    }

    /**
     *  다른 앱 위에 표시 권한 dialog
     */
    private fun showPermissionDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title))
            .setMessage(getString(R.string.dialog_message))
            .setPositiveButton(getString(R.string.dialog_positive_button)) { dialog, which ->
                setPermission()
            }
            .setNegativeButton(getString(R.string.dialog_cancel_button)) { dialog, which ->
                Toast.makeText(this, getString(R.string.permission_require_toast), Toast.LENGTH_SHORT).show()
                finish()
            }
        dialog.show()
    }

    private fun setPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID))
            startActivity(intent)
        }
    }


    /**
     *  toolbar 제목 설정.
     */
    private fun initLayout() {
//        binding.toolbar.apply {
//            setSupportActionBar(this)
//            supportActionBar?.setTitle(R.string.toolbar_title_home)
//        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}