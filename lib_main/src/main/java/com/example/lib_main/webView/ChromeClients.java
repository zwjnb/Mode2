package com.example.lib_main.webView;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.http.SslError;
import android.os.Message;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ChromeClients extends WebChromeClient {

    Activity mActivity;

    WebView mWebView;

    private String TAG = "-ChromeClients";

    private int type;

    public ChromeClients(Activity activity, WebView webView, int type){

        this.mActivity = activity;

        this.mWebView = webView;

        this.type = type;

    }

    @Override

    public boolean onJsAlert(WebView webView, String url, String message, JsResult jsResult) {

//若不是WSD则不相应

        if (type != 1) {

            return true;

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(

                mActivity);

        builder.setMessage(message)

                .setPositiveButton("OK", (arg0, arg1) -> arg0.dismiss()).show();

        jsResult.cancel();

        return true;

    }

    @Override

    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {

        WebView tempWebView = new WebView(mActivity);

        tempWebView.setWebViewClient(new WebViewClient() {

            @Override

            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                if (type == 1) {

               //WSD跳转外部浏览器

                    EventUtils.openSystemBrowser(mActivity,request.getUrl());

                }else {

                  //777KU跳转内部浏览器

                    EventUtils.openWindow(mActivity,request.getUrl().toString(),type);

                }

                return true;

            }

            @Override

            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {

                sslErrorHandler.proceed();

            }

        });

        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;

        transport.setWebView(tempWebView);

        resultMsg.sendToTarget();

        return true;

    }

}
