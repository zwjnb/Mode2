package com.example.lib_util.PhotoPicker

import android.Manifest
import android.Manifest.permission.CAMERA
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import java.io.File


class PhotoPickerUtil {
   companion object{
       val CAMERA = arrayOf(
           Permission.WRITE_EXTERNAL_STORAGE,
           Permission.CAMERA,
       )
       private fun choicePhotoWrapper(context:Context) {
           val perms =
               arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
//           XXPermissions.with(context)
//               .permission(CAMERA)
//               .request(object : OnPermissionCallback {
//                   override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
//                       if (!allGranted) {
//                           Toast.makeText(context,"获取部分权限成功，但部分权限未正常授予",1).show()
//                           return
//                       }
//
//                   }
//
//                   override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
//                       if (doNotAskAgain) {
//                           Toast.makeText(context,"被永久拒绝授权，请手动授予权限",1).show()
//                           // 如果是被永久拒绝就跳转到应用权限系统设置页面
//                           XXPermissions.startPermissionActivity(context, permissions)
//                       } else {
//                           Toast.makeText(context,"获取权限失败",1).show()
//                       }
//                   }
//               })


       }
   }
}