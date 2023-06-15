package com.example.lib_main.webView;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JSAndroid {
    private final Activity activity;

    private String TAG = "-JSAndroid";

    public JSAndroid(Activity activity) {

        this.activity = activity;

    }

    @JavascriptInterface

    public void openWebView(String url) {

        Log.d(TAG, "call: window.Android.openWebView" + url);

        EventUtils.openWindow(activity,url,1);

    }

    @JavascriptInterface

    public void closeWebView() {

        Log.d(TAG, "call: window.Android.closeWebView");

        if (activity!=null) {

//            App.INSTANCE.web.remove(activity);

            activity.finish();

        }

    }

    @JavascriptInterface

    public void openAndroid(String url) {

        EventUtils.openSystemBrowser(activity, Uri.parse(url));

        Log.d(TAG, "call: window.Android.openWebView---");

    }

    @JavascriptInterface

    public void eventTracker(String eventType, String eventValues) {

//判断是否是WSD需要监听的事件

        if (EventUtils.needSendWSDFlyerEvent(eventType)) {

            Gson gson = new Gson();

            Type type = new TypeToken<Map<String, Object>>() {

            }.getType();

            Map<String, Object> map = gson.fromJson(eventValues, type);

            Log.e(TAG, "eventTracker: "+map.toString() );

            EventUtils.logEvent(1,activity.getApplicationContext(),eventType, eventValues);

        }

    }

}