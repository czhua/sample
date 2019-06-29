package com.chan.myapplication

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class TestApplication : Application() {

    private val tag = "TestApplication"

    override fun onCreate() {
        super.onCreate()
        LogUtil.d(tag, "#onCreate")
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        LogUtil.d(tag, "#attachBaseContext")
        MultiDex.install(this)
    }
}