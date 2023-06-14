package com.sum.network.api

import com.sum.network.response.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author mingyan.su
 * @date   2023/2/27 19:07
 * @desc   API接口类
 */
interface ApiInterface {
    /**
     * 首页轮播图
     */
    @FormUrlEncoded
    @POST("/api/v5/jfgfc/*")
    suspend fun getHome(     @Field("appkey") appkey: String,
                             @Field("device_number") device_number: String,
    ): BaseResponse<String>?

}