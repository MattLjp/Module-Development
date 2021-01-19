package com.matt.common.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.ApplicationInfo
import com.alibaba.android.arouter.launcher.ARouter

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        initARouter()
    }

    /**
     * 初始化路由
     */
    private fun initARouter() {
        if (isDebugMode) {
            ARouter.openLog() // 打印日志
            ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(context) // 尽可能早，推荐在Application中初始化
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: BaseApplication

        // 是否为debug模式
        val isDebugMode: Boolean
            get() = context.applicationInfo != null && context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

}