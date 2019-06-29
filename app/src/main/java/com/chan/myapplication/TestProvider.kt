package com.chan.myapplication

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle

class TestProvider : ContentProvider() {

    private val tag = "TestProvider"
    override fun onCreate(): Boolean {
        LogUtil.d(tag, "#onCreate")
        return false
    }

    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        LogUtil.d(tag, "#call")
        return super.call(method, arg, extras)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        LogUtil.d(tag, "#insert")
        return null
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        LogUtil.d(tag, "#query")
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        LogUtil.d(tag, "#onBind")
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        LogUtil.d(tag, "#onBind")
        return 0
    }

    override fun getType(uri: Uri): String? {
        LogUtil.d(tag, "#onBind")
        return null
    }
}
