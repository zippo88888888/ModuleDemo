package com.zp.module_user.view_model

import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.MutableLiveData
import com.zp.module_base.bean.CarItemBean
import com.zp.module_base.common.BaseViewModelImpl
import com.zp.module_base.content.getJob
import com.zp.module_base.content.runUI
import com.zp.module_base.content.showToast
import com.zp.module_base.util.L
import com.zp.module_user.R
import com.zp.module_user.adapter.CarAdapter
import com.zp.module_user.model.CarModel
import kotlinx.coroutines.delay
import java.lang.ref.SoftReference
import java.util.*

class CarViewModel : BaseViewModelImpl<CarModel, ArrayList<CarItemBean>>() {

    private var softReference: SoftReference<CarAdapter>? = null

    private var pageNo = 1

    val loadMoreLiveData by lazy {
        MutableLiveData<List<CarItemBean>>()
    }

    // 是否全选状态
    val isSelectAllState by lazy {
        MutableLiveData<Boolean>()
    }

    // 是否是删除状态
    val isDelState by lazy {
        MutableLiveData<Boolean>()
    }

    fun getDelState() = if (isDelState.value == true) {
        isDelState.value = false
        "编辑"
    } else {
        isDelState.value = true
        "完成"
    }

    fun bindCarAdapter(carAdapter: CarAdapter) {
        if (softReference == null) {
            softReference = SoftReference(carAdapter)
        }
    }

    fun onRefresh() {
        pageNo = 1
        getJob {
            delay(1500)
            runUI {
                refreshData.value = model?.getData()
                if (Random().nextBoolean()) noAnyData.value = true
            }
        }
    }

    fun onLoadMore() {
        pageNo ++
        getJob {
            delay(1500)
            runUI {
                if (pageNo >= 3) {
                    noLoadMoreData.value = false
                } else {
                    loadMoreLiveData.value = model?.loadMoreData()
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.car_selectAllBox -> {
                if (v is CheckBox) {
                    isSelectAllState.value = v.isChecked
                }
            }
            R.id.car_submitBtn -> {
                softReference?.get()?.run {
                    val selectData = getSelectData()
                    if (selectData.isEmpty()) {
                        showToast("未选中任何值")
                    } else {
                        showToast("下单的数量：${selectData.size}")
                        L.e("下单的数据：$selectData")
                    }
                }
            }
            R.id.car_delBtn -> {
                softReference?.get()?.run {
                    val selectData = getSelectData()
                    if (selectData.isEmpty()) {
                        showToast("未选中任何值")
                    } else {
                        deleteSelecteData()
                    }
                }
            }
        }
    }

}