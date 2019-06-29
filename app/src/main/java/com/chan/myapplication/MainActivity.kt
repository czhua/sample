package com.chan.myapplication

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.chan.myapplication.nav.NavActivity


class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LogUtil.d(tag, "#onCreate")

        findViewById<Button>(R.id.start_service_btn).setOnClickListener {
            //            startService()
            startActivity(Intent(this, TextViewActivity::class.java))
        }

        findViewById<Button>(R.id.start_foreground_service_btn).setOnClickListener {
            startForegroundTestService()
        }
        findViewById<Button>(R.id.stop_service_btn).setOnClickListener {
            //            stopService()
        }

    }

    private fun startService() {
        LogUtil.d(tag, "#startService")
        startService(false)
    }

    private fun startForegroundTestService() {
        LogUtil.d(tag, "#startForegroundTestService")
        startService(true)
    }

    private fun stopService() {
        LogUtil.d(tag, "#startService")
        val intent = Intent()
        intent.component = ComponentName("com.chan.myapplication", "com.chan.myapplication.TestService")
        stopService(intent)
    }

    private fun startService(isForeground: Boolean) {
        val intent = Intent()
        intent.component = ComponentName("com.chan.myapplication", "com.chan.myapplication.TestService")
        intent.putExtra("down_status", 0)
        intent.putExtra(TestService.IS_FOREGROUND_SERVICE, isForeground)
        if (!isForeground) {
            startService(intent)
            LogUtil.d(tag, "#------------startService")
        } else {
            LogUtil.d(tag, "#------------startForegroundTestService")
            startForegroundService(intent)
        }
    }
}
