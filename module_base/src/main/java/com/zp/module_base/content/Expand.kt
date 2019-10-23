package com.zp.module_base.content

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.collection.ArrayMap
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zp.module_base.BuildConfig
import com.zp.module_base.common.AppManager
import com.zp.module_base.common.BaseModel
import com.zp.module_base.common.BaseViewModelImpl
import com.zp.module_base.util.L
import com.zp.module_base.util.Toaster
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.lang.NullPointerException
import java.text.DecimalFormat
import kotlin.coroutines.*

val IS_DEBUG = BuildConfig.IS_DEBUG

fun Context.jumpActivity(clazz: Class<*>, map: ArrayMap<String, Any>? = null) {
    startActivity(Intent(this, clazz).apply {
        if (!map.isNullOrEmpty()) {
            putExtras(getBundleFormMapKV(map))
        }
    })
}

fun Fragment.jumpActivity(clazz: Class<*>, map: ArrayMap<String, Any>? = null) {
    activity?.jumpActivity(clazz, map)
}

fun Activity.jumpActivity(clazz: Class<*>, requestCode: Int, map: ArrayMap<String, Any>? = null) {
    startActivityForResult(Intent(this, clazz).apply {
        if (!map.isNullOrEmpty()) {
            putExtras(getBundleFormMapKV(map))
        }
    }, requestCode)
}

/**
 * 根据Map 获取 Bundle 扩展
 */
fun getBundleFormMapKV(map: ArrayMap<String, Any>) = Bundle().apply {
    for ((k, v) in map) {
        when (v) {
            is Int -> putInt(k, v)
            is Double -> putDouble(k, v)
            is Float -> putFloat(k, v)
            is Long -> putLong(k, v)
            is Boolean -> putBoolean(k, v)
            is Char -> putChar(k, v)
            is String -> putString(k, v)
            is Serializable -> putSerializable(k, v)
            is Bundle -> putAll(v)
            is Parcelable -> putParcelableArrayList(k, v as ArrayList<out Parcelable>?)
            else -> L.e("Unsupported format")
        }
    }
}

/** 获取全局的ApplicationContext */
fun getAppContext() = AppManager.getInstance().getApplicationContext()

/** 获取状态栏高度 */
fun Context.getStatusBarHeight() = resources.getDimensionPixelSize(
    resources.getIdentifier("status_bar_height", "dimen", "android")
)

fun dip2pxF(dpValue: Float) = dpValue * getAppContext().resources.displayMetrics.density + 0.5f
fun dip2px(dpValue: Float) = dip2pxF(dpValue).toInt()
fun px2dipF(pxValue: Float) = pxValue / getAppContext().resources.displayMetrics.density + 0.5f
fun px2dip(pxValue: Float) = px2dipF(pxValue).toInt()

fun getColorById(colorID: Int) = ContextCompat.getColor(getAppContext(), colorID)
fun getDimenById(dimenID: Int) = getAppContext().resources.getDimension(dimenID)
fun getStringById(stringID: Int) = getAppContext().resources.getString(stringID)

fun getTextValue(any: Any) = try {
    when (any) {
        is Int -> getStringById(any)
        is String -> any
        else -> any.toString()
    }
} catch (e: Exception) {
    any.toString()
}

fun showToast(value: Any, duration: Int = Toaster.SHORT) {
    Toaster.makeTextS(value, duration)
}

fun doubleFro2(value: Double, pattern: String = "0.00") = DecimalFormat(pattern).format(value)

/**
 * ViewModel && LiveData change value
 */
fun <M : BaseModel, T> BaseViewModelImpl<M, T>.defaultObserve(owner: LifecycleOwner, block: (T) -> Unit) {
    refreshData.observe(owner, Observer {
        block(it)
    })
}

/*
    官方建议 使用代理实现当前Activity作用域的协程！！！
    abstract class ScopedActivity: Activity(), CoroutineScope by MainScope() {
        override fun onDestroy() {
            super.onDestroy()
                cancel()
            }
        }
    }
 */

fun ViewModel.defaultLaunch(context: CoroutineContext = Dispatchers.IO, block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch(context) {
        block.invoke(this)
    }
}

/**
 * 获取 Job
 * 默认IO线程
 */
fun getJob(
    context: CoroutineContext = Dispatchers.IO,
    block: suspend CoroutineScope.() -> Unit
) =
    GlobalScope.launch(context) {
        block()
    }

/**
 * 切换到UI线程
 */
suspend fun runUI(block: (CoroutineScope) -> Unit) {
    withContext(Dispatchers.Main) {
        block(this)
    }
}

/**
 * 使用协程 让 网络请求 完成后再继续下面的操作
 */
suspend fun <T> Call<T>.await(): T {
    return suspendCoroutine {
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (body != null && response.isSuccessful) it.resume(body)
                else it.resumeWithException(NullPointerException("Response body is null"))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (IS_DEBUG) t.printStackTrace()
                it.resumeWithException(t)
            }
        })
    }
}

