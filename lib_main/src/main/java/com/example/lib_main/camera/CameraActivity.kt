package com.example.lib_main.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.camera.core.*
import androidx.camera.core.FocusMeteringAction.FLAG_AF
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.core.impl.LensFacingCameraFilter
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_main.base.ARouteManage
import com.example.lib_main.base.BaseActivity
import com.example.lib_main.databinding.ActivityCameraBinding
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.luck.picture.lib.utils.ToastUtils
import java.io.File
import java.util.concurrent.TimeUnit

@Route(path= ARouteManage.CameraActivity)
class CameraActivity:BaseActivity() {
    private   var isFlash:Boolean=false//是否开启闪光灯
    private var isFacing:Boolean=false//切换前后摄像头
    var builder:ImageCapture.Builder?=null
    var cameraSelector:CameraSelector?=null
    private lateinit var imageCapture: ImageCapture
    var cameraProvider:ProcessCameraProvider?=null
    var preview:Preview?=null
    @SuppressLint("RestrictedApi")
    override fun initView(savedInstanceState: Bundle?) {
        var rootView=ActivityCameraBinding.inflate(layoutInflater)
        setContentLayout(rootView.root)
        setToolbar(false)
        take(rootView)

        rootView.take.setOnClickListener {
          var path=  context.getExternalFilesDir("room")!!.absolutePath+"/"+System.currentTimeMillis()/1000+".png"//应用目录
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(File(path)).build()
            imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(error: ImageCaptureException)
                    {
                        ToastUtils.showToast(context,"拍照失败"+error.message);
                        // insert your code here.
                    }
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        // insert your code here.
                        ToastUtils.showToast(context,"拍照成功");
                        context.sendBroadcast(
                            Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(
                                    "file://$path"
                                )
                            )
                        )
                    }
                })
        }
        rootView.flash.setOnClickListener {
            isFlash=!isFlash
            imageCapture.flashMode=if (isFlash)FLASH_MODE_ON else FLASH_MODE_OFF

        }
        rootView.switchCamera.setOnClickListener {
            isFacing=!isFacing
            cameraSelector = CameraSelector.Builder()
                .requireLensFacing(if (isFacing)CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK)
                .build()
            cameraProvider!!.unbindAll()
            cameraProvider!!.bindToLifecycle(
                context as LifecycleOwner, cameraSelector!!, preview, imageCapture)
//            cameraSelector!!.cameraFilterSet.removeAll {
//              true
//            }
//            cameraSelector!!.cameraFilterSet.add(LensFacingCameraFilter(CameraSelector.LENS_FACING_BACK))
        }
    }
    fun take(rootView:ActivityCameraBinding){
        var permissions= arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.RECORD_AUDIO)
        XXPermissions.with(context).permission(permissions).request(object: OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                if (!allGranted) {
                    ToastUtils.showToast(context,"获取部分权限成功，但部分权限未正常授予");
                    return;
                }
                val provider = ProcessCameraProvider.getInstance(context)
                provider.addListener(object:Runnable{
                    @SuppressLint("RestrictedApi")
                    override fun run() {
                        // 相机提供商现在保证可用
                         cameraProvider= provider.get()
                        // 设置预览用例以显示相机预览
                         preview  = Preview.Builder().build()
                        // 设置捕获用例以允许用户拍照。
                        /**
                         * CAPTURE_MODE_MINIMIZE_LATENCY：缩短图片拍摄的延迟时间。
                         * CAPTURE_MODE_MAXIMIZE_QUALITY：提高图片拍摄的图片质量。
                         * 零快门延迟 (CAPTURE_MODE_ZERO_SHOT_LAG
                         *
                         * FLASH_MODE_ON：闪光灯始终处于开启状态。
                        FLASH_MODE_AUTO：在弱光环境下拍摄时，自动开启闪光灯。
                         */
                         builder = ImageCapture.Builder()
                        imageCapture = builder!!
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                            .setFlashMode(if (isFlash)FLASH_MODE_ON else FLASH_MODE_OFF)//闪光灯
                            .build()
                        // 通过要求镜头朝向来选择相机
                         cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()

                        // 将用例附加到具有相同生命周期所有者的相机
                        val camera = cameraProvider!!.bindToLifecycle(
                            context as LifecycleOwner, cameraSelector!!, preview, imageCapture)
//                        val cameraControl = camera.cameraControl
//                        //设置自动对焦
//                        cameraControl.setZoomRatio(1.0f)
//                        //开启手电筒
//                        cameraControl.enableTorch(false)
//                        rootView.previewView.setOnTouchListener(object :OnTouchListener{
//                            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
//                                val createPoint1 =
//                                    rootView.previewView.meteringPointFactory.createPoint(
//                                        p1!!.x,
//                                        p1.y
//                                    )
//                                val createPoint2 =
//                                    rootView.previewView.meteringPointFactory.createPoint(
//                                        p1!!.x*2,
//                                        p1.y*2
//                                    )
//                                val action = FocusMeteringAction.Builder(createPoint1) // default AF|AE|AWB
//                                    // Optionally add meteringPoint2 for AF/AE.
//                                    .addPoint(createPoint2, FLAG_AF)
//                                    // The action is canceled in 3 seconds (if not set, default is 5s).
//                                    .setAutoCancelDuration(3, TimeUnit.SECONDS)
//                                    .build()
//
//                                val result = cameraControl.startFocusAndMetering(action)
//// Adds listener to the ListenableFuture if you need to know the focusMetering result.
//                                result.addListener({
//                                    // result.get().isFocusSuccessful returns if the auto focus is successful or not.
//                                }, ContextCompat.getMainExecutor(context))
//                                return true
//                            }
//                        })

//                        cameraControl.startFocusAndMetering()
                        // 将预览用例连接到预览视图
                        preview!!.setSurfaceProvider(
                            rootView.previewView.getSurfaceProvider())
                    }
                }, ContextCompat.getMainExecutor(context))
                ToastUtils.showToast(context,"授权成功!");
            }

            override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                super.onDenied(permissions, doNotAskAgain)
                ToastUtils.showToast(context,"授权失败")
            }
        })
    }
}