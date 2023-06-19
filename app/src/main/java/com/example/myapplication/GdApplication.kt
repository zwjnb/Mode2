package com.example.myapplication

import android.app.Application
import android.os.Build
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.appsflyer.AppsFlyerLib
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
        //AF埋点
        var appsflyer = AppsFlyerLib.getInstance();
        appsflyer.setDebugLog(false)
        appsflyer.setMinTimeBetweenSessions(0) ;
//set the OneLink template id for share invite Links
        AppsFlyerLib.getInstance() .setAppInviteOneLink("H5hv")
        appsflyer.init("UowJxTMjZpoFv76hJ3xHNZ", null,this)
        appsflyer.start(  this)
    }
}