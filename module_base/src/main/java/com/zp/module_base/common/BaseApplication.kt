package com.zp.module_base.common

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.module_base.content.IS_DEBUG

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppManager.getInstance().init(this)
        if (IS_DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
            ARouter.printStackTrace()
        }
        ARouter.init(this)
    }

}