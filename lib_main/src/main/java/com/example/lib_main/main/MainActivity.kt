package com.example.lib_main.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_main.R
import com.example.lib_main.base.ARouteManage
import com.example.lib_main.base.BaseActivity
import com.example.lib_main.databinding.ActivityMainBinding
import com.example.lib_main.webView.ChromeClients
import com.example.lib_main.webView.EventUtils
import com.example.lib_main.webView.JSAndroid
import com.jingewenku.abrahamcaijin.commonutil.AppExit2Back
import com.roger.catloadinglibrary.CatLoadingView
import com.sum.network.bean.SplachBeans
import kotlinx.coroutines.flow.*
import org.json.JSONException
import org.json.JSONObject


@Route(path = ARouteManage.mainActivity)
class MainActivity : BaseActivity() {
    var mWebView: WebView? = null
    @Autowired(name="splach")
    @JvmField
    var  bean: SplachBeans?=null
    var loading:CatLoadingView?=null
    @SuppressLint("MissingInflatedId")
    override fun initView(savedInstanceState: Bundle?) {
        var rootView = ActivityMainBinding.inflate(LayoutInflater.from(context))
        setContentLayout(rootView.root)
//        setLeftBack()
        setTitle("Weather")
        // 3.ARouter.getInstance().inject(this)方法会自动完成参数注入
        ARouter.getInstance().inject(this);
        loading= CatLoadingView()
        loading!!.show(supportFragmentManager, "")
        mWebView = rootView.webView
        initWebView(bean!!.webview_set, bean!!.wapurl)
        imageBack!!.visibility= View.INVISIBLE
      UpdateDialog(bean!!.version)
    }
fun UpdateDialog(version:Int){
    if (version<=1){
        return
    }
    var dialog= Dialog(context)
    dialog.setCancelable(false);
    val window: Window = dialog.window!!
    if (window != null) {
        window.setGravity(Gravity.CENTER)
        window.setBackgroundDrawableResource(R.color.transparency)
        window.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    var view= LayoutInflater.from(context).inflate(R.layout.dialog_update,null)
    var down= view.findViewById<TextView>(R.id.down)
    down.setOnClickListener {
        val intent: Intent
        try {
            intent = Intent.parseUri(bean!!.downurl, Intent.URI_INTENT_SCHEME)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.component = null
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    dialog.setContentView(view)
    dialog.show()
}
    @SuppressLint("JavascriptInterface")
    fun initWebView(wetSet: String, url: String) {
        mWebView!!.loadUrl(url);
        var settings = mWebView!!.getSettings();
        //设置WebView支持JavaScript
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        //支持多窗口
        settings.setSupportMultipleWindows(true);
        val ANDROID_ID: String =
            Settings.System.getString(contentResolver, Settings.System.ANDROID_ID)
        settings.setUserAgentString(EventUtils.getUseragent(this, "1.0",ANDROID_ID));
        mWebView!!.setHorizontalScrollBarEnabled(false);
        mWebView!!.setVerticalScrollBarEnabled(false);
        //防止跳转外部浏览器
        mWebView!!.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view!!.loadUrl(url!!)
                return true

            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (view!!.progress==100)
                loading!!.dismiss()
            }
        })
        //多窗口跳转时拦截
        mWebView!!.setWebChromeClient(ChromeClients(this, mWebView, getType(wetSet)))

        if (wetSet.equals("WSD")) {
         //监听WSD事件
            mWebView!!.addJavascriptInterface(JSAndroid(this), "Android");
        } else {
         //监听777KU事件
            mWebView!!.addJavascriptInterface(object : Object() {
                @JavascriptInterface
                fun postMessage(tag: String, value: String) {
                    Log.i(TAG, "jsBridge: tag = " + tag + "\tvalue = " + value);
                    //拦截777ku需要监听的事件
                    if (EventUtils.needSendFlyerEvent(tag)) {
                        Log.i(TAG, "postMessage: tag = " + tag);
                        EventUtils.logEvent(getType(wetSet), getApplicationContext(), tag, value);
                    }
                    if (TextUtils.equals(tag, "openWindow")) {
                        try {
                            var jsonObject = JSONObject(value);
                            var url1 = jsonObject.getString("url");
                            EventUtils.openWindow(this@MainActivity, url1, getType(wetSet));
                        } catch (e: JSONException) {
                            e.printStackTrace();
                        }
                    }
                    if (TextUtils.equals(tag, "closeWindow")) {
                        //关闭所有WebActivity
                        mWebView = null
                        finish();
                    }
                }
            }, "jsBridge");
        }
    }

    fun getType(webset: String): Int {
        if (webset.equals("WSD")) {
            return 1
        } else {
            return -1
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() === 0) { //按下的如果是BACK，同时没有重复
            if (mWebView!!.canGoBack()){
                mWebView!!.goBack()
            }else {
                AppExit2Back.exitApp(context)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}