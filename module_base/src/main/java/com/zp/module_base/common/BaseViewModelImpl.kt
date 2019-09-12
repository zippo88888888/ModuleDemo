package com.zp.module_base.common

import android.view.View
import androidx.lifecycle.MutableLiveData

open class BaseViewModelImpl<M : BaseModel, T> : BaseViewModel<M>(), View.OnClickListener {

    /**
     * 使用一个通用的LiveData
     */
    val refreshData by lazy {
        MutableLiveData<T>()
    }

    /**
     * 没有任何数据 true为真
     */
    val noAnyData by lazy {
        MutableLiveData<Boolean>()
    }

    /**
     * （刷新）获取数据 是否出错 true为真
     */
    val refreshDataError by lazy {
        MutableLiveData<Boolean>()
    }

    /**
     * 是否可以加载更多 true为真
     */
    val noLoadMoreData by lazy {
        MutableLiveData<Boolean>()
    }

    /**
     * 加载更多 是否出错 true为真
     */
    val loadMoreDataError by lazy {
        MutableLiveData<Boolean>()
    }

    protected var model: M? = null

    override fun attachModel(model: M?) {
        this.model = model
    }

    override fun detachModel() {
        this.model = null
    }

    override fun onClick(v: View) = Unit

    interface ILiveDataObserveListener {

        /**
         * 没有任何数据
         */
        fun noAnyData()

        /**
         * 没有加载更多的数据了
         */
        fun noLoadMoreData()

        /**
         * 刷新或请求数据出错
         */
        fun refreshDataError()

        /**
         * 加载更多数据出错
         */
        fun loadMoreDataError()

    }

}