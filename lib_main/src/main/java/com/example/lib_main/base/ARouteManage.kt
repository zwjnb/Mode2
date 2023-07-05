package com.example.lib_main.base

import com.alibaba.android.arouter.launcher.ARouter

class ARouteManage {
    companion object{
       const val  mainActivity="/main/mainActity"
        const val  CameraActivity="/camera/CameraActivity"
        fun IntentMain(){
            ARouter.getInstance().build(mainActivity).navigation()
        }
        fun IntentCamera(){
            ARouter.getInstance().build(CameraActivity).navigation()
        }
    }
}