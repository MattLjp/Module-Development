package com.matt.common.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.matt.common.application.BaseApplication

/**
 *
 * @ Author : 廖健鹏
 * @ Time : 2020/9/18
 * @ e-mail : 329524627@qq.com
 * @ Description :
 */
object ToastUtil {

    private val context: Context = BaseApplication.context

    private var toast: Toast? = null


    fun showShort(@StringRes resId: Int) {
        show(resId, Toast.LENGTH_SHORT)
    }


    fun showShort(text: CharSequence) {
        show(text, Toast.LENGTH_SHORT)
    }

    fun showLong(text: CharSequence) {
        show(text, Toast.LENGTH_LONG)
    }

    fun showLong(@StringRes resId: Int) {
        show(resId, Toast.LENGTH_LONG)
    }


    fun show(text: CharSequence, duration: Int) {
        //解决小米手机首次弹出带应用名称
        //吐司信息设为空，再设置提示文字
        toast = toast ?: Toast.makeText(context, "", duration)
        toast?.setText(text)
        toast?.show()
    }

    fun show(text: Int, duration: Int) {
        //解决小米手机首次弹出带应用名称
        //吐司信息设为空，再设置提示文字
        toast = toast ?: Toast.makeText(context, "", duration)
        toast?.setText(text)
        toast?.show()
    }

}