package com.chan.myapplication

import android.util.Log

object LogUtil {
    private const val name = "MyApplication"

    fun d(tag: String, msg: String) {
        Log.d("${name}_$tag", msg)
    }
}