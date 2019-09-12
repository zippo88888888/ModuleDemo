package com.zp.module_file.view

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.zp.module_base.common.BaseActivity
import com.zp.module_base.content.Routes
import com.zp.module_base.content.defaultObserve
import com.zp.module_file.R
import com.zp.module_file.model.FileMainModel
import com.zp.module_file.view_model.FileMainViewModel
import com.zp.module_file.databinding.ActivityFileMainBinding

@Route(path = Routes.FILE_ROUTE_MAIN)
class FileMainActivity : BaseActivity<FileMainModel, ActivityFileMainBinding, FileMainViewModel>() {

    override fun getContentView() = R.layout.activity_file_main

    override fun getViewModelClass() = FileMainViewModel::class.java

    override fun getModel() = FileMainModel()

    override fun initAll(savedInstanceState: Bundle?) {
        setBarTitle("文件下载")

        bindView?.fileMainViewModel = viewModel?.run {
            defaultObserve(this@FileMainActivity) {
                bindView?.cacheSize = it
            }
            dowloadLiveData.observe(this@FileMainActivity, Observer {
                bindView?.dowloadBean = it
            })
            this
        }
    }

}
