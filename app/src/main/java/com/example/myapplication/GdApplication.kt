package com.example.myapplication

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_util.log.LogUtil
import com.jingewenku.abrahamcaijin.commonutil.ActivityLifecycleCallbacks.SimpleActivityLifecycleCallbacks
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
        //全局application赋值
        SumAppHelper.init(this,BuildConfig.DEBUG)
        //获取app退出前后台
        var life=  object : SimpleActivityLifecycleCallbacks(){
            override fun onActivityStarted(p0: Activity) {
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            }

            override fun onActivityResumed(p0: Activity) {
                super.onActivityResumed(p0)
                LogUtil.e("================>前台")
            }

            override fun onActivityStopped(p0: Activity) {
                super.onActivityStopped(p0)
                LogUtil.e("================>后台")
            }
            override fun onActivityDestroyed(p0: Activity) {
                super.onActivityDestroyed(p0)
            }

        }
        SumAppHelper.getApplication().registerActivityLifecycleCallbacks(life)
    }
}