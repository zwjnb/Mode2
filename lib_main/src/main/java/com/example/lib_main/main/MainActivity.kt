package com.example.lib_main.main

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_main.R
import com.example.lib_main.base.ARouteManage
import com.example.lib_main.base.BaseActivity
@Route(path=ARouteManage.mainActivity)
class MainActivity:BaseActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {
    }
}