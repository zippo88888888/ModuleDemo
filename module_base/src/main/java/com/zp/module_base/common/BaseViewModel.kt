package com.zp.module_base.common

import android.content.Intent
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<M : BaseModel> : ViewModel() {

    abstract fun attachModel(model: M?)

    abstract fun detachModel()

    final override fun onCleared() {
        detachModel()
        onDestroy()
        super.onCleared()
    }

    /**
     * ViewModel 销毁调用
     */
    open fun onDestroy() = Unit

    open fun onNewIntent(intent: Intent?) = Unit

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = Unit

    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) = Unit
}