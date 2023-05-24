package com.example.lib_main.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity


/**
 * Author mingyan.su
 * Time   2023/2/20 12:33
 * Desc   Activity基类
 */
abstract class BaseActivity : AppCompatActivity() {
    protected var TAG: String? = this::class.java.simpleName

//    private val dialogUtils by lazy {
//        LoadingUtils(this)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout()
//        //状态栏背景着色
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.drawable.shape_statusbar);

//        //去除灰色遮罩
//        //Android5.0以上
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            var window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }else if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){//Android4.4以上,5.0以下
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }

        initView(savedInstanceState)
        initData()
    }

    /**
     * 设置布局
     */
    open fun setContentLayout() {
        setContentView(getLayoutResId())
    }

    /**
     * 初始化视图
     * @return Int 布局id
     * 仅用于不继承BaseDataBindActivity类的传递布局文件
     */
    abstract fun getLayoutResId(): Int

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