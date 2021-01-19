package com.matt.common.application

import android.app.Activity
import java.util.*

/**
 * @ Author : 廖健鹏
 * @ Time : 2021/1/18
 * @ e-mail : 329524627@qq.com
 * @ Description :
 */
class AppManager {

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity?) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return try {
            activityStack!!.lastElement()
        } catch (e: Exception) {
            //            e.printStackTrace();
            null
        }
    }

    /**
     * 获取当前Activity的前一个Activity
     */
    fun preActivity(): Activity? {
        val index = activityStack!!.size - 2
        return if (index < 0) {
            null
        } else activityStack!![index]
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack!!.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
        }
    }

    /**
     * 移除指定的Activity
     */
    fun removeActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity = null
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        try {
            for (activity in activityStack!!) {
                if (activity!!.javaClass == cls) {
                    finishActivity(activity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!![i]) {
                activityStack!![i]!!.finish()
            }
            i++
        }
        activityStack!!.clear()
    }

    /**
     * 返回到指定的activity
     *
     * @param cls
     */
    fun returnToActivity(cls: Class<*>) {
        while (activityStack!!.size != 0) {
            if (activityStack!!.peek()!!.javaClass == cls) {
                break
            } else {
                finishActivity(activityStack!!.peek())
            }
        }
    }


    /**
     * 结束当前activity之前的所有activity
     */
    fun finishPreAllActivity() {
        for (i in activityStack!!.indices.reversed()) {
            finishActivity(activityStack!![i])
        }
    }


    /**
     * 是否已经打开指定的activity
     *
     * @param cls
     * @return
     */
    fun isOpenActivity(cls: Class<*>): Boolean {
        if (activityStack != null) {
            var i = 0
            val size = activityStack!!.size
            while (i < size) {
                if (cls == activityStack!!.peek()!!.javaClass) {
                    return true
                }
                i++
            }
        }
        return false
    }

    /**
     * 判断某个Activity 界面是否在前台
     *
     * @param className 某个界面名称
     * @return
     */
    fun isForeground(className: String): Boolean {
        val activity = currentActivity()
        return activity != null && activity.javaClass.simpleName == className
    }

    /**
     * 退出应用程序
     *
     * @param isBackground 是否开开启后台运行
     */
    fun AppExit(isBackground: Boolean?) {
        try {
            finishAllActivity()
        } catch (e: Exception) {
        } finally {
            // 注意，如果您有后台程序运行，请不要支持此句子
//            if (!isBackground) {
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);
//            }
        }
    }

    companion object {
        private var activityStack: Stack<Activity?>? = null

        // For Singleton instantiation
        @Volatile
        private var instance: AppManager? = null

        fun getInstance(): AppManager {
            if (instance == null) {
                synchronized(AppManager::class.java) {
                    if (instance == null) {
                        activityStack = Stack()
                        instance = AppManager()
                    }
                }
            }
            return instance!!
        }

    }
}