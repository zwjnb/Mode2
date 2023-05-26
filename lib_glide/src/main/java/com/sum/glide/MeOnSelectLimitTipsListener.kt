package com.sum.glide

import android.content.Context
import com.luck.picture.lib.config.SelectLimitType
import com.luck.picture.lib.config.SelectorConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnSelectLimitTipsListener
import com.luck.picture.lib.utils.ToastUtils
/**
 * 拦截自定义提示
 */
class MeOnSelectLimitTipsListener : OnSelectLimitTipsListener {
    override fun onSelectLimitTips(
        context: Context,
        media: LocalMedia?,
        config: SelectorConfig,
        limitType: Int,
    ): Boolean {
        if (limitType == SelectLimitType.SELECT_MIN_SELECT_LIMIT) {
            ToastUtils.showToast(context, "图片最少不能低于" + config.minSelectNum + "张")
            return true
        } else if (limitType == SelectLimitType.SELECT_MIN_VIDEO_SELECT_LIMIT) {
            ToastUtils.showToast(context, "视频最少不能低于" + config.minVideoSelectNum + "个")
            return true
        } else if (limitType == SelectLimitType.SELECT_MIN_AUDIO_SELECT_LIMIT) {
            ToastUtils.showToast(context, "音频最少不能低于" + config.minAudioSelectNum + "个")
            return true
        }
        return false
    }
}