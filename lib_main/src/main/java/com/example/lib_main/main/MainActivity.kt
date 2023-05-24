package com.example.lib_main.main

import android.os.Bundle
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_main.R
import com.example.lib_main.base.ARouteManage
import com.example.lib_main.base.BaseActivity
import com.sum.glide.setUrl
@Route(path=ARouteManage.mainActivity)
class MainActivity:BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        setContentLayout(R.layout.activity_main)
        setLeftBack()
        setTitle("首页")
        var img=findViewById<ImageView>(R.id.imgView)
        img.setUrl("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png")
    }
}