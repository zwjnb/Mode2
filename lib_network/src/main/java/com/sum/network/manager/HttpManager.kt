package com.sum.network.manager

import android.util.Log
import com.jingewenku.abrahamcaijin.commonutil.netWork.NetworkUtil
import com.sum.framework.helper.SumAppHelper
import com.sum.network.constant.BASE_URL
import com.sum.network.error.NoNetWorkException
import com.sum.network.error.ERROR
import com.sum.network.interceptor.CookiesInterceptor
import com.sum.network.interceptor.HeaderInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author mingyan.su
 * @date   2023/2/23 21:29
 * @desc  网络请求管理类
 */
object HttpManager {
    private val mRetrofit: Retrofit

    init {
        //服务地址列表
         var baseUrls = arrayOf(  "https://l2dsvcgt.buzz/",//主服务地址
                 "https://j4sdxcqw.buzz/",
                 "https://j4qwmjhy.buzz/",
                 "https://j5fdxoop.buzz/",
                 "https://j5xdstyg.buzz/",
                 "https://j6fdsqxu.buzz/",
                 "https://j2qwefds.buzz/",
                 "https://j7hgvdqt.buzz/",
                 "https://j7bvfdqi.buzz/",
                 "https://j8jhvdqe.buzz/",
                 "https://j8mhbfsu.buzz/")

        ;
        mRetrofit = Retrofit.Builder()
                .client(initOkHttpClient())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    /**
     * 获取 apiService
     */
    fun <T> create(apiService: Class<T>): T {
        return mRetrofit.create(apiService)
    }

    /**
     * 初始化OkHttp
     */
    private fun initOkHttpClient(): OkHttpClient {
        val build = OkHttpClient.Builder()
                .connectTimeout(12, TimeUnit.SECONDS)
                .writeTimeout(12, TimeUnit.SECONDS)
                .readTimeout(12, TimeUnit.SECONDS)
        // 添加参数拦截器
        val interceptors = mutableListOf<Interceptor>()
        build.addInterceptor(CookiesInterceptor())
        build.addInterceptor(HeaderInterceptor())

        //日志拦截器
        val logInterceptor = HttpLoggingInterceptor { message: String ->
            if (SumAppHelper.isDebug()) {
                Log.i("okhttp", "data:$message")
            }
        }
        if (SumAppHelper.isDebug()) {
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        }
        build.addInterceptor(logInterceptor)
        //网络状态拦截
        build.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                if (NetworkUtil.isConnected(SumAppHelper.getApplication())) {
                    val request = chain.request()
                    return chain.proceed(request)
                } else {
                    throw NoNetWorkException(ERROR.NETWORD_ERROR)
                }
            }
        })
        return build.build()
    }

}