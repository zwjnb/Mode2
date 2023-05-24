package com.example.lib_main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.example.lib_main.base.ARouteManage
import com.example.lib_main.base.BaseActivity
import com.example.lib_util.DownTimer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplachActivity:BaseActivity() {


    override fun initView(savedInstanceState: Bundle?) {
        setContentLayout(R.layout.activity_splach)
        setToolbar(false)
        setFitsSystemWindows(false)
        window.setBackgroundDrawable(null)

     var button=findViewById<Button>(R.id.button)

     //倒计时
     DownTimer.downTimer(3, start = {
         button.setText("3秒")
     }, completion = {
         button.setText("0秒")
         ARouteManage.IntentMain()
         finish()
     }, each = {
         button.setText("${it}秒")
     }, lifecycleScope )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }
}