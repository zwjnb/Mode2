package com.example.lib_main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.lib_main.base.ARouteManage
import com.example.lib_main.base.BaseActivity
import com.example.lib_main.bean.SplachBean
import com.example.lib_main.databinding.ActivitySplachBinding
import com.example.lib_main.repository.LoginRepository
import com.example.lib_util.log.LogUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr
import com.jingewenku.abrahamcaijin.commonutil.encryption.EncryptUtils
import com.sum.glide.setUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SplachActivity:BaseActivity() {
    val loginRepository by lazy { LoginRepository() }
    var rootView:ActivitySplachBinding?=null
    override fun initView(savedInstanceState: Bundle?) {
         rootView=  ActivitySplachBinding.inflate(LayoutInflater.from(context))
        setContentLayout(rootView!!.root)
        setToolbar(false)
        setFitsSystemWindows(false)
        window.setBackgroundDrawable(null)

     var button= rootView!!.button
        lifecycleScope.launch {
            flow<String> {
               var json= loginRepository.Home("e4b3dc7b23652624118d9c769dfb5625","UhqqumgfpiF1qg8vnI")
                if (json != null) {
                    emit(json)
                }
            }
            .flowOn(Dispatchers.Main)
            .onEach {
//             AppToastMgr.ToastShortCenter(it)
            }.collect {
               var json= EncryptUtils.aes256ECBPkcs7PaddingDecrypt(it,"jaix8WnfqRFpQlLk")
               var gson=Gson()
               var bean= gson.fromJson(json, object : TypeToken<SplachBean>() {}.getType()) as SplachBean
               var bakcImgUrl=bean.splash.replace("\"","")
               rootView!!.image.setUrl(bakcImgUrl)
                if (bean.iswap==0){//不跳转

                }else{//跳转
                    ARouteManage.IntentMain()
                    finish()
                }

            }
        }

//     //倒计时
//     DownTimer.downTimer(1, start = {
//         button.setText("1秒")
//     }, completion = {
//         button.setText("0秒")
//         ARouteManage.IntentMain()
//         finish()
//     }, each = {
//         button.setText("${it}秒")
//     }, lifecycleScope )
    }
fun GsonJson(json:String){

}
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }
}