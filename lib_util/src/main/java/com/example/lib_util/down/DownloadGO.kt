package com.example.lib_util.down

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.*
import java.net.UnknownHostException


object DownloadGO {
    /**
     * @param url 文件下载连接
     * @param targetParent 需要保存到指定文件夹目录（tips: 是文件夹）
     */
    fun download(url: String, targetParent: String,context:Context): Flow<DownloadStatus> {

        var bufferedInputStream: BufferedInputStream? = null
        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null
        return flow {
            //下载前先检查 是否已经存在本地了
            val localFile = checkDownloadFile(url, targetParent)
            if (localFile != null) {
                //这里表示本地已经存在了已经下载好的文件
                Log.e("Down","文件已经存在")
                emit(DownloadStatus.onSuccess(localFile))

            } else {
                //执行下载

                val request = Request.Builder().url(url).get().build()
                val response = OkHttpClient.Builder().build().newCall(request).execute()
                val code = response.code()


                Log.e("Down","download: code :${code}")
                //网络有响应成功，且body 不为空
                Log.e("Down","download: response.isSuccessful :${response.isSuccessful}")

                if (response.isSuccessful  ) {
                    saveFile(url, targetParent, response,context).collect {
                        emit(it)
                    }

                } else {
                    //请求失败的情况
                    if (code == 404) {
                        emit(DownloadStatus.onUrlUnkownFail())
                    }
                    emit(DownloadStatus.onFail(null))
                }
            }

        }.catch { error ->
            //捕获到异常了
            Log.e("Down","catch:error:${error}")

            if (error is UnknownHostException) {
                //无网络
                emit(DownloadStatus.onNetFail())
            }
            emit(DownloadStatus.onFail(null))

        }.onCompletion {
            bufferedInputStream?.close()
            outputStream?.close()
            inputStream?.close()
        }.flowOn(Dispatchers.IO)
    }

    fun saveFile(
        url: String,
        targetParent: String,
        response: Response, context: Context,
    ): Flow<DownloadStatus> {

        var bufferedInputStream: BufferedInputStream? = null
        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null

        Log.e("Down","1-2")

        return flow {
            val body = response.body()
            val contentLength:Long = body!!.contentLength()
            inputStream = body!!.byteStream()
            Log.e("Down","contentLength:${contentLength}")
            //使用临时文件保存
//            val tmpFile = getFileTmpName(url)
            val tmpFile = "123.apk"
            val file = File(targetParent, tmpFile)
            outputStream = FileOutputStream(file)
            val bufferSize = 1024 * 8
            val buffer = ByteArray(bufferSize)
            bufferedInputStream = BufferedInputStream(inputStream, bufferSize)
            var readLength: Int
            var currentLength = 0L
            var oldProgress = 0L
            while (bufferedInputStream!!.read(buffer, 0, bufferSize)
                    .also { readLength = it } != -1
            ) {

                outputStream!!.write(buffer, 0, readLength)
                currentLength += readLength

                val currentProgress = currentLength * 100 / contentLength
//                Log.e("Down","currentProgress:${currentProgress} \n currentLength:${currentLength}")
                if (currentProgress - oldProgress >= 1) {
                    oldProgress = currentProgress
                    emit(DownloadStatus.onProgress(currentProgress))
                }

            }


            emit(DownloadStatus.onProgress(100L))

//            //修改文件名字
//            val newFile = updataFile(file, targetParent, url)
            //返回下载成功
            emit(DownloadStatus.onSuccess(file))
            installApp(file,context)
        }.onCompletion {
            bufferedInputStream?.close()
            outputStream?.close()
            inputStream?.close()
        }.catch { error ->
            Log.e("Down","error:${error}")

        }

    }

    /**
     * 调起安装
     * @param file
     */
     fun installApp(file: File,context: Context) {
                 XXPermissions.with(context)
                .permission(Permission.REQUEST_INSTALL_PACKAGES)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                        if (!allGranted) {
                            Toast.makeText(context,"获取部分权限成功，但部分权限未正常授予",1).show()
                            return
                        }
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {//小于7.0直接安装
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
                        context.startActivity(intent)
                            //获取apk的路径信息
                        }else {
                            val uri = FileProvider.getUriForFile(context.applicationContext, context.getPackageName() + ".fileprovider",file)
                            val intent = Intent()
                            //设置预览在一个新的任务中显示
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.action = Intent.ACTION_VIEW
                            //将数据和类型设置进入到intent里
                            intent.setDataAndType(uri, "application/vnd.android.package-archive")
                            //设置读写权限
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                            context.startActivity(intent)
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                        if (doNotAskAgain) {
                            Toast.makeText(context,"被永久拒绝授权，请手动授予权限",1).show()
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(context, permissions)
                        } else {
                            Toast.makeText(context,"获取权限失败",1).show()
                        }
                    }
                })

    }

    fun updataFile(oldFile: File, targetPath: String, downloadUrl: String): File {

        val fileType = downloadUrl.substring(downloadUrl.lastIndexOf("."))
        Log.e("Down","fileType:${fileType}")
        var fileName = Md5.md5String(downloadUrl);
        fileName = "${fileName}${fileType}"

        //修改文件名为正式的
        val newPathFile = File(targetPath, fileName)
        oldFile.renameTo(newPathFile)

        return if (newPathFile.exists()) {
            newPathFile
        } else {
            oldFile
        }

    }

    //检查要下载的文件是否已经存在本地了
    private fun checkDownloadFile(fileUrl: String, outputFile: String): File? {

        val fileType = fileUrl.substring(fileUrl.lastIndexOf("."))

        var fileName = Md5.md5String(fileUrl);

        fileName = "${fileName}${fileType}"
        val localFile = File(outputFile, fileName)

        return if (localFile.exists()) {
            localFile
        } else {
            null
        }
    }

    //临时文件
    private fun getFileTmpName(fileUrl: String): String {
        return "${Md5.md5String(fileUrl)}.tmp"
    }
}
