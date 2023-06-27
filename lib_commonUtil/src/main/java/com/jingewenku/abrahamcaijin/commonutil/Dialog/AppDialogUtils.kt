package com.jingewenku.abrahamcaijin.commonutil.Dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.jingewenku.abrahamcaijin.commonutil.R

class AppDialogUtils {
    companion object{
        /**
         *
         */
        fun dialog(context: Context,content:String){
            dialog(context,content,Gravity.CENTER)
        }
        fun dialog(context: Context,content:String,gravity: Int){
            var dialog=Dialog(context)
            var view= LayoutInflater.from(context).inflate(R.layout.layout_dialog,null)
            var dilogSure= view.findViewById<TextView>(R.id.dilog_sure)
            var dilogCancle= view.findViewById<TextView>(R.id.dilog_cancle)
            var dilogContent= view.findViewById<TextView>(R.id.dilog_content)
            dilogContent.setText(content)
            dialog.setContentView(view)
            dialog.setCanceledOnTouchOutside(false);//禁止外部点击隐藏
            var window=dialog.window
            window!!.setGravity(gravity)
            if (gravity==Gravity.BOTTOM||gravity==Gravity.TOP){
                window!!.attributes.width= ViewGroup.LayoutParams.MATCH_PARENT
                window!!.attributes.height= ViewGroup.LayoutParams.WRAP_CONTENT
            }else{
                window!!.attributes.width= ViewGroup.LayoutParams.WRAP_CONTENT
                window!!.attributes.height= ViewGroup.LayoutParams.WRAP_CONTENT
            }

            window!!.setBackgroundDrawableResource(R.color.transparency)
            dialog.show()
            dilogSure.setOnClickListener {
                dialog.dismiss()
            }
            dilogCancle.setOnClickListener {
                dialog.dismiss()
            }
        }
    }
}