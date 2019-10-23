package com.zp.module_file.view_model

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zp.module_base.bean.DowloadBean
import com.zp.module_base.common.BaseViewModelImpl
import com.zp.module_base.content.*
import com.zp.module_base.util.L
import com.zp.module_base.util.MyFileUtil
import com.zp.module_file.R
import com.zp.module_file.model.FileMainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FileMainViewModel : BaseViewModelImpl<FileMainModel, String>() {

    private var isDowloading = false

    val dowloadLiveData by lazy {
        MutableLiveData<DowloadBean>()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fileMain_dowloadBtn -> {
                if (isDowloading) {
                    showToast("下载中，请稍后...")
                } else {
                    startDownload()
                }
            }
            R.id.fileMain_getCacheSizeBtn -> {
                MyFileUtil.getCacheSize {
                    refreshData.value = "${doubleFro2(it)}MB"
                }
            }
            R.id.fileMain_clearCacheBtn -> {
                if (isDowloading) {
                    showToast("下载中，请稍后...")
                } else {
                    MyFileUtil.deleteCache {
                        refreshData.value = "清除成功"
                    }
                }
            }
        }
    }

    private fun startDownload() {
        defaultLaunch {
            try {
                val responseBody = model?.downloadFile()?.await()!!
                val fileDir = MyFileUtil.getPathForPath(MyFileUtil.OTHERS)
                val fileName = MyFileUtil.getFileName(".apk")
                L.e("文件下载地址：${fileDir + fileName}")
                model?.startDownloadFile(responseBody, fileDir, fileName) { dowloadState, dowloadSize, fileSize ->
                    when (dowloadState) {
                        0 -> {
                            isDowloading = false
                            showToast("下载完成")
                        }
                        1 -> {
                            isDowloading = true
                            val progress = (dowloadSize.toFloat() / fileSize.toFloat()) * 100.0
                            val formatFileSize = doubleFro2(fileSize / 1024.0 / 1024.0)
                            val formatDowloadSize = doubleFro2(dowloadSize / 1024.0 / 1024.0)
                            val leftDowloadStr = "${formatDowloadSize}MB/${formatFileSize}MB"
                            val rightDowloadStr = "${progress.toInt()}%/100%"
                            dowloadLiveData.value =
                                DowloadBean(fileSize, dowloadSize, progress.toInt(), leftDowloadStr, rightDowloadStr)
                        }
                        else -> {
                            isDowloading = false
                            showToast("下载失败")
                        }
                    }
                }
            } catch (e: Exception) {
                if (IS_DEBUG) e.printStackTrace()
                isDowloading = false
                L.e(" model?.downloadFile() 请求出错 ")
            }
        }
    }

}