package com.example.lib_main.main

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_main.R
import com.example.lib_main.base.ARouteManage
import com.example.lib_main.base.BaseActivity
import com.example.lib_main.databinding.ActivityMainBinding
import com.example.lib_util.down.DownloadGO
import com.example.lib_util.down.DownloadStatus
import com.example.lib_util.log.LogUtil
import com.jingewenku.abrahamcaijin.commonutil.ActivityLifecycleCallbacks.SimpleActivityLifecycleCallbacks
import com.jingewenku.abrahamcaijin.commonutil.AppExit2Back
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.utils.MediaUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File


@Route(path=ARouteManage.mainActivity)
class MainActivity:BaseActivity() {
    var img:ImageView?=null
    override fun initView(savedInstanceState: Bundle?) {
       var rootView=ActivityMainBinding.inflate(LayoutInflater.from(context))
        setContentLayout(rootView.root)
        setLeftBack()
        setTitle("首页")
        img=rootView.imgView
//      img.setUrl("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png")
        var button=rootView.button
        button.setOnClickListener {
            download(this)
        }
      var life=  object :SimpleActivityLifecycleCallbacks(){
          override fun onActivityStarted(p0: Activity) {
          }

          override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
          }

          override fun onActivityCreated(p0: Activity, p1: Bundle?) {
          }

          override fun onActivityResumed(p0: Activity) {
              super.onActivityResumed(p0)
              AppToastMgr.ToastShortCenter("onActivityResumed")
          }

          override fun onActivityDestroyed(p0: Activity) {
              super.onActivityDestroyed(p0)
              AppToastMgr.ToastShortCenter("onActivityDestroyed")
          }

      }

//        PictureSelector.create(this)
//            .openGallery(SelectMimeType.ofImage())
//            .setImageEngine(GlideEngine.createGlideEngine())
//            .setCropEngine(ImageFileCropEngine(false))//图片剪裁
//            .setSelectLimitTipsListener( MeOnSelectLimitTipsListener())//拦截自定义提示
//            .forResult(object : OnResultCallbackListener<LocalMedia?> {
//                override fun onResult(result: ArrayList<LocalMedia?>?) {
//                    analyticalSelectResults(result)
//                }
//                override fun onCancel() {
//
//                }
//            })

   lifecycleScope.launch {
       flow<String> {
           emit("1")
       }.flowOn(Dispatchers.IO)
           .onCompletion {  }
           .onStart {  }
           .collect {}
   }
    }

    override fun onDestroy() {
        super.onDestroy()

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() === 0) { //按下的如果是BACK，同时没有重复
            AppExit2Back.exitApp(context)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    /**
     * 处理选择结果
     *
     * @param result
     */
    private fun analyticalSelectResults(result: ArrayList<LocalMedia?>?) {
        for (media in result!!) {
            if (media!!.width == 0 || media.height == 0) {
                if (PictureMimeType.isHasImage(media.mimeType)) {
                    val imageExtraInfo = MediaUtils.getImageSize(context, media.path)
                    media.width = imageExtraInfo.width
                    media.height = imageExtraInfo.height
                } else if (PictureMimeType.isHasVideo(media.mimeType)) {
                    val videoExtraInfo = MediaUtils.getVideoSize(context, media.path)
                    media.width = videoExtraInfo.width
                    media.height = videoExtraInfo.height
                }
            }
            if (media.isCut)
            img!!.setImageURI(Uri.parse(media.cutPath!!))
            else
                img!!.setImageURI(Uri.parse(media.realPath!!))

//            Log.i(MainActivity.TAG, "文件名: " + media.fileName)
//            Log.i(MainActivity.TAG, "是否压缩:" + media.isCompressed)
//            Log.i(MainActivity.TAG, "压缩:" + media.compressPath)
//            Log.i(MainActivity.TAG, "初始路径:" + media.path)
//            Log.i(MainActivity.TAG, "绝对路径:" + media.realPath)
//            Log.i(MainActivity.TAG, "是否裁剪:" + media.isCut)
//            Log.i(MainActivity.TAG, "裁剪路径:" + media.cutPath)
//            Log.i(MainActivity.TAG, "是否开启原图:" + media.isOriginal)
//            Log.i(MainActivity.TAG, "原图路径:" + media.originalPath)
//            Log.i(MainActivity.TAG, "沙盒路径:" + media.sandboxPath)
//            Log.i(MainActivity.TAG, "水印路径:" + media.watermarkPath)
//            Log.i(MainActivity.TAG, "视频缩略图:" + media.videoThumbnailPath)
//            Log.i(MainActivity.TAG, "原始宽高: " + media.width + "x" + media.height)
//            Log.i(MainActivity.TAG, "裁剪宽高: " + media.cropImageWidth + "x" + media.cropImageHeight)
        }

    }
    @OptIn(InternalCoroutinesApi::class)
    fun download(context: Context) {
        val time = System.currentTimeMillis()
        LogUtil.d("time1:${time}")
        val url = "http://wzwaz.ddooo.com/tl12306_202555.apk?key=bd6dfb20d05c0a81266d9c70389a57fe&uskey=d6e85bc69a37f571eb09b99e760a29ce"
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