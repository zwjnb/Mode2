package com.example.lib_util.down

import java.io.File


sealed class DownloadStatus {

    /**
     * 下载进度
     */
    data class onProgress(val progress: Long?) : DownloadStatus()

    /**
     * 下载成功
     */
    data class onSuccess(val file: File?) : DownloadStatus()

    /**
     * 网络错误（网络断开或未连接）
     */
    class onNetFail() : DownloadStatus()

    /**
     * 下载地址错误
     */
    class onUrlUnkownFail() : DownloadStatus()
    /**
     * 下载错误
     */
    data class onFail(val error: Throwable?) : DownloadStatus()

}