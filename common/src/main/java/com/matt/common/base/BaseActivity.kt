package com.matt.common.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.matt.common.application.AppManager

/**
 * @ Author : 廖健鹏
 * @ Time : 2021/1/18
 * @ e-mail : 329524627@qq.com
 * @ Description :
 */
abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var mContext: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 加载内容布局
        setContentView(onBindLayout())
        ARouter.getInstance().inject(this)
        mContext = this
        initView()
        initData()
        AppManager.getInstance().addActivity(this)
    }

    abstract fun onBindLayout(): View

    abstract fun initView()

    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
        AppManager.getInstance().removeActivity(this)
    }

}