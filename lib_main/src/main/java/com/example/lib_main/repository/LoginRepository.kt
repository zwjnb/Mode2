package com.example.lib_main.repository

import com.sum.network.manager.ApiManager
import com.sum.network.repository.BaseRepository

/**
 * @author mingyan.su
 * @date   2023/3/24 18:36
 * @desc   登录仓库
 */
class LoginRepository : BaseRepository() {

    suspend fun Home(appkey: String, device_number: String): String? {
        return requestResponse {
            ApiManager.api.getHome(appkey,device_number)
        }
    }


}