package com.chan.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TestReceiver : BroadcastReceiver() {

    private val tag = "TestReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        LogUtil.d(tag, "#onReceive")

    }
}