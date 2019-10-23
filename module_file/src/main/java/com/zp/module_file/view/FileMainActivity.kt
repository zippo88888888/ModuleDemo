package com.zp.module_file.view

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.zp.module_base.common.BaseActivity
import com.zp.module_base.content.Routes
import com.zp.module_base.content.defaultObserve
import com.zp.module_file.R
import com.zp.module_file.databinding.ActivityFileMainBinding
import com.zp.module_file.model.FileMainModel
import com.zp.module_file.view_model.FileMainViewModel

@Route(path = Routes.FILE_ROUTE_MAIN)
class FileMainActivity : BaseActivity<FileMainModel, ActivityFileMainBinding, FileMainViewModel>() {

    override fun getContentView() = R.layout.activity_file_main

    override fun getViewModelClass() = FileMainViewModel::class.java

    override fun getModel() = FileMainModel()

    override fun initAll(savedInstanceState: Bundle?) {
        setBarTitle("文件下载")


        /*

         1、
         如果你看到一位朋友戴着耳机从你身边匆匆而过，没有跟你打招呼，
         千万不要生气，也许TA并不是讨厌你，只是害怕社交而已。当然，还有一个
         可能是......近视且没有戴眼镜！

         2、
         有趣的灵魂千千万，但是你的却最好看

         */
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
