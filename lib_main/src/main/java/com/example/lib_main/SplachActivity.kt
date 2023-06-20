package com.example.lib_main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.example.lib_main.base.ARouteManage
import com.example.lib_main.base.BaseActivity
import com.example.lib_main.databinding.ActivitySplachBinding
import com.example.lib_main.repository.LoginRepository
import com.example.lib_util.DownTimer
import com.example.lib_util.log.LogUtil
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr
import com.sum.network.error.ExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplachActivity:BaseActivity() {
    val loginRepository by lazy { LoginRepository() }

    override fun initView(savedInstanceState: Bundle?) {
      var rootView=  ActivitySplachBinding.inflate(LayoutInflater.from(context))

        setContentLayout(rootView.root)
        setToolbar(false)
        setFitsSystemWindows(false)
        window.setBackgroundDrawable(null)

     var button=rootView.button
     //倒计时
     DownTimer.downTimer(1, start = {
         button.setText("1秒")
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