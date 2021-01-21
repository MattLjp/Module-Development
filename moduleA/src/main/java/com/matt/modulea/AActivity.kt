package com.matt.modulea

import androidx.databinding.ViewDataBinding
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.matt.common.base.BaseActivity
import com.matt.common.bean.Test
import com.matt.common.constants.ARouterConfig
import com.matt.modulea.databinding.ActivityABinding


/**
 * @ Author : 廖健鹏
 * @ Time : 2021/1/18
 * @ e-mail : 329524627@qq.com
 * @ Description :
 */
@Route(path = ARouterConfig.A_ARouter)
class AActivity : BaseActivity() {
    // 为每一个参数声明一个字段，并使用 @Autowired 标注
    @Autowired
    @JvmField
    var name: String = ""

    // 通过name来映射URL中的不同参数
    @Autowired(name = "age")
    @JvmField
    var key2 = 0

    // 支持解析自定义对象，URL中使用json传递
    @Autowired
    @JvmField
    var key3: Test? = null

    private val binding by lazy {
        ActivityABinding.inflate(layoutInflater)

    }

    override fun onBindLayout() = binding.root

    override fun initView() {
//        ARouter.getInstance().inject(this)
        binding.tvName.text = key3?.name
        binding.tvAge.text = key3?.age.toString()
    }

    override fun initData() {
    }
}