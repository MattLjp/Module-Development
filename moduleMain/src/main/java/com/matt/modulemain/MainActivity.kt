package com.matt.modulemain

import com.alibaba.android.arouter.launcher.ARouter
import com.matt.common.base.BaseActivity
import com.matt.common.bean.Test
import com.matt.common.constants.ARouterConfig
import com.matt.modulemain.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)

    }

    override fun onBindLayout() = binding.root

    override fun initView() {
        binding.btnIntoA.setOnClickListener {
            ARouter.getInstance().build(ARouterConfig.A_ARouter)
                .withString("name", "Jack")
                .withInt("age", 23)
                .withSerializable("key3", Test("David", 25))
                .navigation()
        }
        binding.btnIntoB.setOnClickListener {
            ARouter.getInstance().build(ARouterConfig.B_ARouter).navigation()
        }
        binding.btnIntoC.setOnClickListener {
            ARouter.getInstance().build(ARouterConfig.C_ARouter).navigation()
        }
    }

    override fun initData() {
    }
}