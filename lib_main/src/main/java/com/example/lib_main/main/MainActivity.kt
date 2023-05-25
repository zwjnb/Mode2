package com.example.lib_main.main

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_main.R
import com.example.lib_main.base.ARouteManage
import com.example.lib_main.base.BaseActivity
import com.example.lib_util.down.DownloadGO
import com.example.lib_util.down.DownloadStatus
import com.example.lib_util.log.LogUtil
import com.sum.glide.setUrl
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

@Route(path=ARouteManage.mainActivity)
class MainActivity:BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        setContentLayout(R.layout.activity_main)
        setLeftBack()
        setTitle("首页")
        var img=findViewById<ImageView>(R.id.imgView)
        img.setUrl("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png")
        var button=findViewById<Button>(R.id.button)
        button.setOnClickListener {
            download(this)
        }

    }
    @OptIn(InternalCoroutinesApi::class)
    fun download(context: Context) {
        val time = System.currentTimeMillis()
        LogUtil.d("time1:${time}")
        val url = "http://wzwaz.ddooo.com/ysb_158880.apk?key=d46ee589457a3094ab2b7ec14c6caab1&uskey=a30b004761eed01f09889d5addae32c5"
        val file2 = getTargetParentFile(context)
        lifecycleScope.launch {
            DownloadGO.download(
                url = url,
                targetParent = file2.path,context
            ).collect {
                when (it) {
                    is DownloadStatus.onProgress -> {
                        LogUtil.d("currentThread:${Thread.currentThread().name}")
                        LogUtil.d("progress：${it.progress}")
                    }

                    is DownloadStatus.onSuccess -> {
                        LogUtil.d("耗时:${System.currentTimeMillis() - time}")
                        LogUtil.d("currentThread:${Thread.currentThread().name}")
                        LogUtil.d("onSuccess file：${it.file?.path}")
                    }

                    is DownloadStatus.onFail -> {
                        LogUtil.d("下载失败 error：${it?.error}")
                    }
                    else -> {
                        LogUtil.d("下载失败")
                    }
                }
            }

            LogUtil.d("处理完成")
        }
    }

    fun getTargetParentFile(context: Context): File {
        val file0 = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.getAbsolutePath();
        val file1 = File("${file0}")
        if (!file1.exists() || !file1.isDirectory) {
            file1.mkdirs()
        }
        return file1
    }
}