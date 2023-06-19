package com.example.lib_main.webView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lib_main.R;
import com.example.lib_main.base.BaseActivity;
import com.example.lib_main.databinding.ActivityWebBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class WebActivity extends BaseActivity {

    private String TAG = "WebActivity";

    WebView mWebView;
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        ActivityWebBinding rootView= ActivityWebBinding.inflate(LayoutInflater.from(this));
        setContentLayout(rootView.getRoot());
        setLeftBack();
        setTitle("");
        String url = getIntent().getStringExtra("url");
      getImageBack().setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (mWebView.canGoBack()){
                  mWebView.goBack();
              }else{
                  finish();
              }
          }
      });
        int isWsd = getIntent().getIntExtra("isWsd", 0);

        mWebView = rootView.webView;

        if (TextUtils.isEmpty(url)) {

            finish();

        } else {

            initWebView(isWsd, url);

        }
    }



    @SuppressLint("SetJavaScriptEnabled")

    private void initWebView(int isWsd, String url) {

        Log.e(TAG, "initWebView: " + url);

        mWebView.loadUrl(url);

        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);

        settings.setAllowFileAccess(true);

        settings.setDomStorageEnabled(true);

        settings.setSupportMultipleWindows(true);
        String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        settings.setUserAgentString(EventUtils.getUseragent(this, "1.0", ANDROID_ID));

        mWebView.setWebChromeClient(new ChromeClients(this, mWebView,isWsd));

        mWebView.setWebViewClient(new WebViewClient() {

            @Override

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);

                return true;

            }

        });

        mWebView.setHorizontalScrollBarEnabled(false);

        mWebView.setVerticalScrollBarEnabled(false);

        if (isWsd == 1) {

            mWebView.addJavascriptInterface(new JSAndroid(this), "Android");

        } else {

            mWebView.addJavascriptInterface(new Object() {

                @JavascriptInterface

                public void postMessage(String tag, String value) {

                    Log.i(TAG, "jsBridge: tag = " + tag + "\tvalue = " + value);

                    if (EventUtils.needSendFlyerEvent(tag)) {

                        Log.i(TAG, "postMessage: tag = " + tag);

                        EventUtils.logEvent(isWsd,getApplicationContext(),tag, value);

                    }

                    if (TextUtils.equals(tag,"openWindow")) {

                        try {

                            JSONObject jsonObject = new JSONObject(value);

                            String url1 = jsonObject.getString("url");

                            EventUtils.openWindow(WebActivity.this,url1, isWsd);

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    if (TextUtils.equals(tag,"closeWindow")) {
                       finish();
                    }

                }

            }, "jsBridge");

        }

    }

    @Override

    protected void onDestroy() {

        super.onDestroy();
          finish();
//        App.INSTANCE.web.remove(this);

    }

    @Override

    public void onBackPressed() {

        if (mWebView.canGoBack()) {

            mWebView.goBack();

        } else {

            super.onBackPressed();

        }

    }


}

