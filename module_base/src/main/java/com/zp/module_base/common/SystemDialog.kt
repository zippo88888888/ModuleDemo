package com.zp.module_base.common

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import java.lang.ref.SoftReference

typealias PositiveListener = (DialogInterface?) -> Unit
typealias NegativeListener = (DialogInterface?) -> Unit
typealias NeutralListener = (DialogInterface?) -> Unit

class SystemDialog(
    context: Context,
    private var isUserDefaultTitle: Boolean = true,
    private var isCancel: Boolean? = null
) {

    private val reference: SoftReference<Context> by lazy { SoftReference(context) }

    private fun createDialog(listener1: PositiveListener, str: Array<out String>): AlertDialog.Builder? {
        if (reference.get() != null) {
            return AlertDialog.Builder(reference.get()!!).apply {
                if (isUserDefaultTitle) {
                    setTitle("温馨提示")
                    setMessage(str[0])
                    setPositiveButton(str[1]) { dialog, _ ->
                        dialog.dismiss()
                        listener1.invoke(dialog)
                    }
                } else {
                    setTitle(str[0])
                    setMessage(str[1])
                    setPositiveButton(str[2]) { dialog, _ ->
                        dialog.dismiss()
                        listener1.invoke(dialog)
                    }
                }
                if (isCancel != null) setCancelable(isCancel!!)
            }
        } else return null
    }

    /**
     * 显示一个 按钮的Dialog
     * @param positiveListener     第一个按钮点击的监听
     */
    fun showDialog1(positiveListener: PositiveListener, vararg str: String) {
        createDialog(positiveListener, str)?.apply { show() }
    }

    /**
     * 显示两个 按钮的Dialog
     * @param positiveListener     第一个按钮点击的监听
     * @param negativeListener     第二个按钮点击的监听
     */
    fun showDialog2(positiveListener: PositiveListener, negativeListener: NegativeListener, vararg str: String) {
        createDialog(positiveListener, str)?.apply {
            setNegativeButton(if (isUserDefaultTitle) str[2] else str[3]) { dialog, _ ->
                dialog.dismiss()
                negativeListener.invoke(dialog)
            }
            show()
        }
    }

    /**
     * 显示三个 按钮的Dialog
     * @param positiveListener     第一个按钮点击的监听
     * @param negativeListener     第二个按钮点击的监听
     * @param neutralListener      第三个按钮点击的监听
     */
    fun showDialog3(
        positiveListener: PositiveListener,
        negativeListener: NegativeListener,
        neutralListener: NeutralListener,
        vararg str: String
    ) {
        createDialog(positiveListener, str)?.apply {
            setNegativeButton(if (isUserDefaultTitle) str[2] else str[3]) { dialog, _ ->
                dialog.dismiss()
                negativeListener.invoke(dialog)
            }
            setNeutralButton(if (isUserDefaultTitle) str[3] else str[4]) { dialog, _ ->
                dialog.dismiss()
                neutralListener.invoke(dialog)
            }
            show()
        }
    }

}