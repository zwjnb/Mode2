package com.jingewenku.abrahamcaijin.commonutil;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.EOFException;


/**
 * 主要功能： 自定义Toast提示框
 *
 * @Prject: CommonUtilLibrary
 * @Package: com.jingewenku.abrahamcaijin.commonutil
 * @author: AbrahamCaiJin
 * @date: 2017年05月04日 14:13
 * @Copyright: 个人版权所有
 * @Company:
 * @version: 1.0.0
 */
public class AppToastMgr {

	
	//对话框时长号(毫秒)
	private static int duration = 200;
	
	//自定义toast对象
	private static Toast toast;
	static TextView toastText;
	static View view;

	/**
	 * 屏幕中心位置长时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void ToastLongCenter(Context context, String message) {
		if (context != null) {
			if (toast==null) {
				toast = new Toast(context);
			}

			if (view==null){
				view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
				toastText=view.findViewById(R.id.tv_toast_message);
				toastText.setText(message);
			}else{
				toastText.setText(message);
			}

			//根据自己需要对view设置text或其他样式
			toast.setView(view);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.show();
		}

	}
	/**
	 * 屏幕中心位置短时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void ToastShortCenter(Context context, String message) {
		if (context != null) {
			if (toast==null) {
				toast = new Toast(context);
			}

			if (view==null){
				view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
				toastText=view.findViewById(R.id.tv_toast_message);
				toastText.setText(message);
			}else{
				toastText.setText(message);
			}

			//根据自己需要对view设置text或其他样式
			toast.setView(view);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	/**
	 * 屏幕底部位置短时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void ToastShortBottom(Context context, String message) {
		if (context != null) {
			if (toast==null) {
				toast = new Toast(context);
			}

			if (view==null){
				view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
				toastText=view.findViewById(R.id.tv_toast_message);
				toastText.setText(message);
			}else{
				toastText.setText(message);
			}

			//根据自己需要对view设置text或其他样式
			toast.setView(view);
			toast.setGravity(Gravity.BOTTOM, 0, 0);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		}

	}
	/**
	 * 屏幕底部位置长时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void ToastLongBottom(Context context, String message) {
		if (context != null) {
			if (toast==null) {
				toast = new Toast(context);
			}

			if (view==null){
				view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
				toastText=view.findViewById(R.id.tv_toast_message);
				toastText.setText(message);
			}else{
				toastText.setText(message);
			}

			//根据自己需要对view设置text或其他样式
			toast.setView(view);
			toast.setGravity(Gravity.BOTTOM, 0, 0);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.show();
		}

	}
}
