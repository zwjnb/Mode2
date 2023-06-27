package com.jingewenku.abrahamcaijin.commonutil.File

import android.content.Context
import android.os.Environment
import com.example.lib_util.log.LogUtil
import java.io.File
import java.io.FileOutputStream

/**
 * 文件存储
 */
class FileWriterUtils {
    companion object{

        fun FileWriter(context: Context,content:String,fileName:String){
            var path= Environment.getExternalStorageDirectory()//根目录  存储根目录需要权限
            var absolutePath=context.getExternalFilesDir("room")!!.absolutePath//应用目录

            var file= File(absolutePath,fileName)
            if (!file.isDirectory)
                file.createNewFile()
            var out: FileOutputStream?=null
            try {
                out= FileOutputStream(file)
                out.write(content.toByteArray())
                LogUtil.e("==================>成功")
            }catch (e:java.lang.Exception){
                e.printStackTrace()
                LogUtil.e("==================>失败"+e.printStackTrace())
            }finally {
                if (out!=null){
                    out.close()
                }
            }

        }

    }
}