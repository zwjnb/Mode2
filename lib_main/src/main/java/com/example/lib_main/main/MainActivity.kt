package com.example.lib_main.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_main.base.ARouteManage
import com.example.lib_main.base.BaseActivity
import com.example.lib_main.databinding.ActivityMainBinding
import com.example.lib_util.down.DownloadGO
import com.example.lib_util.down.DownloadStatus
import com.example.lib_util.log.LogUtil
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.jingewenku.abrahamcaijin.commonutil.AppExit2Back
import com.jingewenku.abrahamcaijin.commonutil.Dialog.AppDialogUtils
import com.jingewenku.abrahamcaijin.commonutil.File.FileWriterUtils
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.MediaUtils
import com.luck.picture.lib.utils.ToastUtils
import com.sum.glide.GlideEngine
import com.sum.glide.MeOnSelectLimitTipsListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter


@Route(path=ARouteManage.mainActivity)
class MainActivity:BaseActivity() {
    var img:ImageView?=null
    override fun initView(savedInstanceState: Bundle?) {
       var rootView=ActivityMainBinding.inflate(LayoutInflater.from(context))
        setContentLayout(rootView.root)
        setLeftBack()
        setTitle("首页")
        img=rootView.imgView
//        img!!.setUrl("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png")
        var button=rootView.button
        button.setOnClickListener {
            download(this)
        }
        rootView.fileSave.setOnClickListener {
            var content="具体改了什么呢？其实就是两个API： TelecomManager 类中的 getLine1Number() 方法 TelecomManager 类中的 getMsisdn() 方法\n" +
                    "\n" +
                    "也就是当用到这两个API的时候，原来的READ_PHONE_STATE权限不管用了，需要READ_PHONE_NUMBERS权限才行。\n" +
                    "\n" +
                    "下面具体说说，targetSdkVersion修改到30，然后运行一个获取电话号码的程序："
            //文件存储
            FileWriterUtils.FileWriter(context,content,"文档.txt")

//            var path= Environment.getExternalStorageDirectory()//根目录
//            var absolutePath=   getExternalFilesDir("room")!!.absolutePath//应用目录
//            LogUtil.e("==================>"+absolutePath)
//            val STORAGE = arrayOf(
////            Permission.READ_EXTERNAL_STORAGE,
////            Permission.WRITE_EXTERNAL_STORAGE,
//                Permission.MANAGE_EXTERNAL_STORAGE
//            )
        }
        rootView.selectImage.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .isDisplayCamera(false)//是否显示相机按钮
                .isWithSelectVideoImage(false)//是否同时选择视频、图片返回
                .setMinSelectNum(1)//最小选择数量
                .setMaxSelectNum(5)//最大选择数量
//              .setCropEngine(ImageFileCropEngine(false))//图片剪裁
                .setSelectLimitTipsListener( MeOnSelectLimitTipsListener())//拦截自定义提示
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>?) {
                        analyticalSelectResults(result)
                    }
                    override fun onCancel() {

                    }
                })
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "image/*" // 相片类
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) //多选
//
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            startActivityForResult(intent, 2)
        }
        rootView.layoutDialog.setOnClickListener {
            AppDialogUtils.dialog(context,"提示语测试")
        }


        flow<String> {
            emit("1")
        }.flowOn(Dispatchers.IO)
            .onCompletion {  }
            .onStart {  }
            .launchIn(lifecycleScope)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            // 处理错误
            return
        }
        when (requestCode) {
            1 -> {
                //单选模式得到图片.
                val currentUri: Uri = data!!.data!!

                // 在这里处理photo/video URI。返回的应该是provider的路径，需要转成file
                return
            }
            2 -> {
                // 多选模式得到图片.
                var i = 0
                data!!.clipData!!.itemCount
//                for(i  until data.clipData!!.itemCount){
//                    var uri:Uri=data.clipData!!.getItemAt(i).uri;
//                    // 在这里处理每一个photo/video URI。返回的应该是provider的路径，需要转成file
//                }
                return;
            }
    }}
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