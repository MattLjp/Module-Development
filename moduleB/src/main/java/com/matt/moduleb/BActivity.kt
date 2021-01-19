package com.matt.moduleb

import com.alibaba.android.arouter.facade.annotation.Route
import com.matt.common.base.BaseActivity
import com.matt.common.constants.ARouterConfig
import com.matt.moduleb.databinding.ActivityBBinding

/**
 * @ Author : 廖健鹏
 * @ Time : 2021/1/18
 * @ e-mail : 329524627@qq.com
 * @ Description :
 */
@Route(path = ARouterConfig.B_ARouter)
class BActivity : BaseActivity() {
    private val binding by lazy {
        ActivityBBinding.inflate(layoutInflater)

    }

    override fun onBindLayout() = binding.root

    override fun initView() {
    }

    override fun initData() {
    }
}