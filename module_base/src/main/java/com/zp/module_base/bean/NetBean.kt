package com.zp.module_base.bean

data class DowloadBean(
    var fileSize: Long = 0L,
    var dowloadSize: Long = 0L,
    var dowloadProgress: Int = 0,
    var leftSizeStr: String = "",
    var rightSizeStr: String = ""
)


data class CarItemBean(
    var title: String = "",
    var content: String = "",
    var picUrl: String = "",
    var dateStr: String = "",
    var price: Long = 0L,
    var count: Int = 0,
    var checked: Boolean = false
)