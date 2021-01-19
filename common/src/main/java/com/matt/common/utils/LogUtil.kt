package com.matt.common.utils

import android.util.Log
import com.matt.common.application.BaseApplication


/**
 * 日志工具类
 *
 */
object LogUtil {

    val IsNeedLog: Boolean = BaseApplication.isDebugMode


    /**
     * 日志输出时的TAG
     */
    private const val TAG = "LogUtil"


    fun v(msg: String) {
        if (IsNeedLog) {
            Log.v(TAG, msg)
        }
    }


    fun d(msg: String) {
        if (IsNeedLog) {
            Log.v(TAG, msg)
        }
    }


    fun i(msg: String) {
        if (IsNeedLog) {
            Log.i(TAG, msg)
        }
    }

    fun w(msg: String) {
        if (IsNeedLog) {
            Log.v(TAG, msg)
        }
    }

    fun e(msg: String) {
        if (IsNeedLog) {
            Log.v(TAG, msg)
        }
    }


}