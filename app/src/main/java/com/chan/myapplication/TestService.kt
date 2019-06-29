package com.chan.myapplication

import android.app.Notification
import android.app.Notification.PRIORITY_MIN
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class TestService : Service() {

    private val tag = "TestService"
    private var handler: MyHandler? = null

    companion object {
        const val IS_FOREGROUND_SERVICE = "IS_FOREGROUND_SERVICE"
    }

    override fun onBind(intent: Intent?): IBinder? {
        LogUtil.d(tag, "#onBind")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        LogUtil.d(tag, "#onCreate")
        handler = MyHandler()
//        Thread.sleep(5000)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtil.d(tag, "#onStartCommand")

        LogUtil.d(tag, "#onStartCommand-------------2")

        var isForegroundService = false
        if (intent != null) {
            isForegroundService = intent.getBooleanExtra(IS_FOREGROUND_SERVICE, false)
        }

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        if (isForegroundService) {
            startForeground(101, notification)
            LogUtil.d(tag, "startForeground")
        }
//        Thread.sleep(10000)
        LogUtil.d(tag, "isForegroundService: $isForegroundService")
     /*   object : Thread() {
            override fun run() {
                super.run()
                try {
                    LogUtil.d(tag, "startForeground sleep start")
                    Thread.sleep(30000)
                    LogUtil.d(tag, "startForeground sleep end")
                    handler?.sendEmptyMessage(startId)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()*/


        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelId = "my_service"
        val channelName = "My Background Service"
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_HIGH
        )
        chan.lightColor = Color.BLUE
        chan.importance = NotificationManager.IMPORTANCE_NONE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }


    override fun onDestroy() {
        LogUtil.d(tag, "#onDestroy")
        super.onDestroy()
        stopForeground(true)
    }


    private inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            LogUtil.d(tag, "handleMessage: ${msg.what}")
            stopSelf(msg.what)
        }
    }

}