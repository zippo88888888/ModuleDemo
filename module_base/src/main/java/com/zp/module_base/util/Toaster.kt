package com.zp.module_base.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.zp.module_base.content.getAppContext
import com.zp.module_base.content.getTextValue

class Toaster(con: Context) : Toast(con) {

    companion object {

        const val T = Gravity.TOP
        const val B = Gravity.BOTTOM
        const val C = Gravity.CENTER

        const val SHORT = Toast.LENGTH_SHORT
        const val LONG = Toast.LENGTH_LONG

        private var toast: Toast? = null

        private fun checkToast() {
            if (toast != null) toast?.cancel()
        }

        /**
         * 系统自带的 消息提醒
         */
        fun makeTextS(str: Any, duration: Int = SHORT) {
            checkToast()
            toast = makeText(getAppContext(), getTextValue(str), duration)
            toast?.show()
        }

    }
}