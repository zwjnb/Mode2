package com.example.lib_main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.lib_main.base.ARouteManage
import com.example.lib_main.base.BaseActivity
import com.example.lib_main.databinding.ActivitySplachBinding
import com.example.lib_main.repository.LoginRepository
import com.example.lib_util.log.LogUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr
import com.jingewenku.abrahamcaijin.commonutil.encryption.EncryptUtils
import com.roger.catloadinglibrary.CatLoadingView
import com.sum.glide.setUrl
import com.sum.network.bean.SplachBeans
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class SplachActivity:BaseActivity() {
    val loginRepository by lazy { LoginRepository() }
    var rootView:ActivitySplachBinding?=null
    override fun initView(savedInstanceState: Bundle?) {
         rootView=  ActivitySplachBinding.inflate(LayoutInflater.from(context))
        setContentLayout(rootView!!.root)
        setToolbar(false)
        setFitsSystemWindows(false)
        window.setBackgroundDrawable(null)
        var loading= CatLoadingView()
        var button= rootView!!.button
        lifecycleScope.launch {
            flow<String> {
               var json= loginRepository.Home("cb45f568168184269d29d007b7f3847c","EcAntUwQ6TTwPySo")
                if (json != null) {
                    emit(json)
                }
            }
            .onStart {
                loading.show(supportFragmentManager, "")
            }
            .flowOn(Dispatchers.Main)
            .onEach {
//             AppToastMgr.ToastShortCenter(it)
            }.onCompletion {
                    loading.dismiss()
            }.collect {
               var json= EncryptUtils.aes256ECBPkcs7PaddingDecrypt(it,"EcAntUwQ6TTwPySo")
                LogUtil.e("=============>"+json)
               var gson=Gson()
               var bean= gson.fromJson(json, object : TypeToken<SplachBeans>() {}.getType()) as SplachBeans
               var bakcImgUrl=bean.splash.replace("\"","")
               rootView!!.image.setUrl(bakcImgUrl)
                if (bean.iswap==0){//不跳转

                }else{//跳转
                    ARouteManage.IntentMain(bean)
                    finish()
                }

            }
        }


    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }
}