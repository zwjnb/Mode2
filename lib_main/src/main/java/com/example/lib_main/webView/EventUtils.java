package com.example.lib_main.webView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.WebSettings;

import androidx.annotation.NonNull;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EventUtils {
    private static String[] EVENTS = {

            "firstrecharge",

            "login",

            "logout",

            "registerClick",

            "rechargeClick",

            "register",

            "recharge",

            "withdrawClick",

            "withdrawOrderSuccess",

            "firstrecharge",

            "login",

            "logout",

            "recharge",

            "rechargeClick",

            "register",

            "registerClick",

            "withdrawClick",

            "withdrawOrderSuccess"

    };

    private static String[] WSD_EVENTS = {

            "firstOpen",

            "registerSubmit",

            "register",

            "depositSubmit",

            "firstDeposit",

            "withdraw",

            "firstDepositArrival",

            "deposit"

    };

    public static HashMap<String, String> ADJUST_EVENT = new HashMap<>();

    private static final String TAG = "";

    public static boolean needSendFlyerEvent(String event) {

        return Arrays.asList(EVENTS).contains(event);

    }

    public static boolean needSendWSDFlyerEvent(String event) {

        return Arrays.asList(WSD_EVENTS).contains(event);

    }

    public static String getUseragent(final Context context, String version, String uuid) {

        String userAgent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            try {

                userAgent = WebSettings.getDefaultUserAgent(context);

            } catch (Exception e) {

                userAgent = System.getProperty("http.agent");

            }

        } else {

            userAgent = System.getProperty("http.agent");

        }

        final StringBuilder sb = new StringBuilder();

        assert userAgent != null;

        for (int i = 0, length = userAgent.length(); i < length; i++) {

            char c = userAgent.charAt(i);

            if (c <= '\u001f' || c >= '\u007f') {

                sb.append(String.format("\\u%04x", (int) c));

            } else {

                sb.append(c);

            }

        }

        String replace = sb.toString().replace("; wv", "; xx-xx");

        return String.format("%s/%s AppShellVer:%s UUID/%s", replace, Build.BRAND, version, uuid);

    }

    public static void logEvent(int t, Context app, String tag, String value) {

//WSD不需要特殊数据处理，直接全部上传即可

        Gson gson = new Gson();

        Type type = new TypeToken<Map<String, Object>>() {

        }.getType();

        Map<String, Object> eventValues = gson.fromJson(value, type);

//777KU数据上传

        if (t == 2 ) {

            eventValues = new HashMap<>();

            eventValues.put(AFInAppEventParameterName.CONTENT_ID, tag);

            eventValues.put(AFInAppEventParameterName.CONTENT_TYPE, tag);

            eventValues.put(AFInAppEventParameterName.CONTENT, value);

            try {

                JSONObject jsonObject = new JSONObject(value);

                String amount = jsonObject.optString("amount");

                eventValues.put(AFInAppEventParameterName.REVENUE, amount);

                eventValues.put(AFInAppEventParameterName.CURRENCY, "PHP");

                Log.i(TAG, "logEvent: amount = " + amount);

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

////数据上传

        AppsFlyerLib.getInstance().logEvent(app,

                tag, eventValues, new AppsFlyerRequestListener() {

                    @Override

                    public void onSuccess() {

                        Log.i(TAG, "AppsFlyerLib onSuccess: ");

                    }

                    @Override

                    public void onError(int i, @NonNull String s) {

                        Log.i(TAG, "AppsFlyerLib onError: " + s);

                    }

                });

    }

//跳转外部浏览器

    public static void openSystemBrowser(Context context, Uri uri) {

        Intent intent;

        try {

            intent = Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME);

            intent.addCategory(Intent.CATEGORY_BROWSABLE);

            intent.setComponent(null);

            context.startActivity(intent);

        } catch (Exception e) {

            Log.e(TAG, "openSystemBrowser failure", e);

        }

    }

//跳转内部浏览器

    public static void openWindow(Activity context, String url, int t) {

//300ms内连续跳转不响应，用以适配新777KU支付跳转异常

//这里不能粗暴的将WebActivity设置singleTop或者singleTask

//        if (App.INSTANCE.isFast()) {
//
//            return;
//
//        }

        Intent intent = new Intent(context, WebActivity.class);

        intent.putExtra("url", url);

        intent.putExtra("isWsd", t);

        context.startActivityForResult(intent,1);

    }

}
