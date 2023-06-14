package com.example.myapplication

import android.app.Application
import android.os.Build
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr
import com.sum.framework.helper.SumAppHelper

class GdApplication :MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
        //toast上下文赋值
        AppToastMgr.init(this)
        SumAppHelper.init(this,BuildConfig.DEBUG)
    }
}