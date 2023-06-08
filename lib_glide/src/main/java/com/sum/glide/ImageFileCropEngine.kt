package com.sum.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.luck.picture.lib.config.SelectLimitType
import com.luck.picture.lib.config.SelectorConfig
import com.luck.picture.lib.engine.CropFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnSelectLimitTipsListener
import com.luck.picture.lib.utils.ToastUtils
import com.yalantis.ucrop.UCrop

class ImageFileCropEngine: CropFileEngine {
    var isCheckCircle:Boolean?=null//是否支持圆形剪裁
    constructor(checkCircle:Boolean){
        isCheckCircle=checkCircle
    }
    override fun onStartCrop(
        fragment: Fragment?,
        srcUri: Uri?,
        destinationUri: Uri?,
        dataSource: ArrayList<String>?,
        requestCode: Int,
    ) {

        val options: UCrop.Options = buildOptions(fragment!!)
        val uCrop: UCrop = UCrop.of(srcUri!!, destinationUri!!, dataSource!!)
        uCrop.withOptions(options)
        uCrop.setImageEngine(object : com.yalantis.ucrop.UCropImageEngine {
            override fun loadImage(context: Context?, url: String?, imageView: ImageView?) {
                Glide.with(context!!).load(url).override(180, 180).into(imageView!!)
            }

            override fun loadImage(
                context: Context?,
                url: Uri?,
                maxWidth: Int,
                maxHeight: Int,
                call: com.yalantis.ucrop.UCropImageEngine.OnCallbackListener<Bitmap>?,
            ) {
                Glide.with(context!!).asBitmap().load(url).override(maxWidth, maxHeight)
                    .into(object : CustomTarget<Bitmap?>() {

                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?,
                        ) {
                            call?.onCall(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            call?.onCall(null)
                        }
                    })
            }


        })
        uCrop.start(fragment!!.requireActivity(), fragment, requestCode)
    }

    /**
     * 配制UCrop，可根据需求自我扩展
     *
     * @return
     */
    private fun buildOptions(fragment:Fragment): UCrop.Options {
        val options = UCrop.Options()
        options.setHideBottomControls(true)
        options.setFreeStyleCropEnabled(true)
        options.setShowCropFrame(true)
        options.setShowCropGrid(true)
        //圆形剪裁框
        options.setCircleDimmedLayer(isCheckCircle!!)
        options.withAspectRatio(100f, 100f)
        options.setStatusBarColor(ContextCompat.getColor(fragment.requireContext(), com.sum.framework.R.color.white))
//        options.setCropOutputPathDir(getSandboxPath())
//        options.isCropDragSmoothToCenter(false)
//        options.setSkipCropMimeType(*getNotSupportCrop())
//        options.isForbidCropGifWebp(cb_not_gif.isChecked())
        options.isForbidSkipMultipleCrop(true)
        options.setMaxScaleMultiplier(100f)
//        if (selectorStyle != null && selectorStyle.getSelectMainStyle().getStatusBarColor() != 0) {
//            val mainStyle: SelectMainStyle = selectorStyle.getSelectMainStyle()
//            val isDarkStatusBarBlack = mainStyle.isDarkStatusBarBlack
//            val statusBarColor = mainStyle.statusBarColor
//            options.isDarkStatusBarBlack(isDarkStatusBarBlack)
//            if (StyleUtils.checkStyleValidity(statusBarColor)) {
//                options.setStatusBarColor(statusBarColor)
//                options.setToolbarColor(statusBarColor)
//            } else {
//                options.setStatusBarColor(
//                    ContextCompat.getColor(
//                        getContext(),
//                        R.color.ps_color_grey
//                    )
//                )
//                options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey))
//            }
//            val titleBarStyle: TitleBarStyle = selectorStyle.getTitleBarStyle()
//            if (StyleUtils.checkStyleValidity(titleBarStyle.titleTextColor)) {
//                options.setToolbarWidgetColor(titleBarStyle.titleTextColor)
//            } else {
//                options.setToolbarWidgetColor(
//                    ContextCompat.getColor(
//                        getContext(),
//                        R.color.ps_color_white
//                    )
//                )
//            }
//        } else {
//            options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey))
//            options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey))
//            options.setToolbarWidgetColor(
//                ContextCompat.getColor(
//                    getContext(),
//                    R.color.ps_color_white
//                )
//            )
//        }
        return options
    }


}