package com.example.myapplication

import android.app.Application
import android.os.Build
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter

class GdApplication :MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }
}