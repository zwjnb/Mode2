package com.example.myapplication

import android.app.Application
import android.os.Build
import com.alibaba.android.arouter.launcher.ARouter
import com.ceshi.demo.BuildConfig

class GdApplication :Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }
}