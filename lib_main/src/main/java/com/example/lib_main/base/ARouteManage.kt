package com.example.lib_main.base

import com.alibaba.android.arouter.launcher.ARouter

class ARouteManage {
    companion object{
       const val  mainActivity="/main/mainActity"
        fun IntentMain(){
            ARouter.getInstance().build(mainActivity).navigation()
        }
    }
}