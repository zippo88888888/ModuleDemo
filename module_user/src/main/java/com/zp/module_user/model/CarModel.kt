package com.zp.module_user.model

import com.zp.module_base.bean.CarItemBean
import com.zp.module_base.common.BaseModel
import java.util.ArrayList

class CarModel : BaseModel() {

    fun getData() = ArrayList<CarItemBean>().apply {
        for (i in 10..29) {
            add(
                CarItemBean(
                    "我是标题${i + 1}",
                    getColorType(i),
                    getPicUrl(i),
                    "2019-8-${i + 1} 14:20:$i",
                    10L * i,
                    i,
                    false
                )
            )
        }
    }

    fun loadMoreData() = getData()

    private fun getPicUrl(index: Int) =
        when {
            index % 2 == 0 -> "https://image.shutterstock.com/image-vector/vector-set-icon-computer-chips-600w-1106058749.jpg"
            index % 3 == 0 -> "https://www.computerhope.com/jargon/c/cpu.jpg"
            index % 5 == 0 -> "https://2d.zol-img.com.cn/product/185_320x240/837/ceZClYuk5wnc.jpg"
            else -> "https://avatars3.githubusercontent.com/u/19178963?s=460&v=4"
        }

    private fun getColorType(index: Int) =
        when {
            index % 2 == 0 -> "黑色-M"
            index % 3 == 0 -> "灰色-L"
            index % 5 == 0 -> "白色-XL"
            else -> "五彩黑-S"
        }

}