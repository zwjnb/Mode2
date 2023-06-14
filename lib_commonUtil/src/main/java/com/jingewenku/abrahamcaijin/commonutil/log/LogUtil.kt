package com.example.lib_util.log

import android.util.Log

class LogUtil {
    companion object{

        private const val TAG = "日志"

        fun d(content:Any){
            d(TAG,content)
        }

        fun d(tag:String,content:Any){
                Log.d(tag, content as String)
        }

        @JvmStatic
        fun e(content:Any){
            e(TAG,content)
        }

        fun e(tag:String,content:Any){
                Log.e(tag, content as String)
        }


    }

}