package com.jingewenku.abrahamcaijin.commonutil.ActivityLifecycleCallbacks

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

abstract class SimpleActivityLifecycleCallbacks : ActivityLifecycleCallbacks{
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityResumed(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }
}