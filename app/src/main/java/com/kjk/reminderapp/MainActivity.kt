package com.kjk.reminderapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.kjk.reminderapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        Log.d(TAG, "onCreate: ${Build.VERSION.SDK_INT}")

        if (!Settings.canDrawOverlays(this)) {
            checkPermission()
        }

        /*
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                Log.d(TAG, "onCreate: 허용됨")
            }
            shouldShowRequestPermissionRationale(Manifest.permission.SYSTEM_ALERT_WINDOW) -> {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
                Log.d(TAG, "onCreate: 권한을 허용해야 합니다.")
        }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                Log.d(TAG, "onCreate: 요청허용팝업 ")
                //requestPermissionLauncher.launch(Manifest.permission.SYSTEM_ALERT_WINDOW)
//               requestPermissions(this,
//                    arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW),
//                    112
//                )
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID))
                startActivity(intent)
            }
        }

         */

        initLayout()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID))
            startActivityForResult(intent, 112)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 112) {
            if (!Settings.canDrawOverlays(this)) {
                checkPermission()
            } else {
                Log.d(TAG, "onActivityResult: ${"권한 허용됨"}")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            112 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Log.d(TAG, "onRequestPermissionsResult: granted")
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Log.d(TAG, "onRequestPermissionsResult: ")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun initLayout() {
        binding.toolbar.apply {
            setSupportActionBar(this)
            supportActionBar?.setTitle(R.string.toolbar_title_home)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}