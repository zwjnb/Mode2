package com.example.lib_main.base

import com.alibaba.android.arouter.launcher.ARouter
import com.sum.network.bean.SplachBeans

class ARouteManage {
    companion object{
       const val  mainActivity="/main/mainActity"
        fun IntentMain(bean: SplachBeans){
            ARouter.getInstance().build(mainActivity)
                .withSerializable("splach",bean)
                .navigation()
        }
    }
}