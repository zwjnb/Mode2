package com.example.lib_main.base

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.lib_main.R
import com.example.lib_main.databinding.ActivityToorbarBinding
import com.gyf.immersionbar.ImmersionBar
import com.jingewenku.abrahamcaijin.commonutil.application.AppUtils


/**
 * Author mingyan.su
 * Time   2023/2/20 12:33
 * Desc   Activity基类
 */
abstract class BaseActivity : AppCompatActivity() {
    protected var TAG: String? = this::class.java.simpleName
    var layout:FrameLayout?=null
    var context:Context=this
    var inflater:LayoutInflater?=null
    var frame_layout:FrameLayout?=null
    var toolbar: ConstraintLayout?=null
    var imageBack:ImageView?=null
    var toolbarTitle:TextView?=null
//    private val dialogUtils by lazy {
//        LoadingUtils(this)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.init(context)
        //沉浸式状态栏
        //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f)          .init();
        //初始化基础布局文件
        layout=FrameLayout(this)
        layout?.id= R.id.main_container
        layout?.fitsSystemWindows=true
        setContentView(layout)
        initView(savedInstanceState)
        initData()
    }

    /**
     * 设置布局
     */
    open fun setContentLayout(childView:View) {
        if (inflater==null) {
            inflater = LayoutInflater.from(context)
        }
        var rootView= ActivityToorbarBinding.inflate(LayoutInflater.from(context),layout,true)
        frame_layout=rootView.frameLayout
        toolbar=rootView.toolbar
        toolbarTitle=rootView.title
        imageBack=rootView.imgBack
        //添加子布局
        if (childView!=null) {
          frame_layout?.addView(childView)
        }
    }

    /**
     * 是否展示Toolbar
     */
    open fun setToolbar(isVisibility:Boolean){
        toolbar?.isVisible=isVisibility;

    }

    /**
     * 设置是否布局距离上面状态栏高度
     */
    open fun setFitsSystemWindows(fitSystemWindows:Boolean){
        layout?.fitsSystemWindows=fitSystemWindows
    }

    /**
     * 设置标题
     */
    open fun setTitle(title:String){
        toolbarTitle?.setText(title)
    }

    /**
     * 设置左返回按钮点击事件
     */
    open fun setLeftBack(){
     imageBack?.setOnClickListener {
         finish()
     }
 }

    /**
     * 初始化布局
     * @param savedInstanceState Bundle?
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    open fun initData() {

    }

//    /**
//     * 加载中……弹框
//     */
//    fun showLoading() {
//        showLoading(getString(R.string.default_loading))
//    }
//
//    /**
//     * 加载提示框
//     */
//    fun showLoading(msg: String?) {
//        dialogUtils.showLoading(msg)
//    }

//    /**
//     * 加载提示框
//     */
//    fun showLoading(@StringRes res: Int) {
//        showLoading(getString(res))
//    }
//
//    /**
//     * 关闭提示框
//     */
//    fun dismissLoading() {
//        dialogUtils.dismissLoading()
//    }

//    /**
//     * Toast
//     * @param msg Toast内容
//     */
//    fun showToast(msg: String) {
//        TipsToast.showTips(msg)
//    }
//
//    /**
//     * Toast
//     * @param resId 字符串id
//     */
//    fun showToast(@StringRes resId: Int) {
//        TipsToast.showTips(resId)
//    }

    override fun onDestroy() {
        super.onDestroy()
    }
}