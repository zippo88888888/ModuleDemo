package com.zp.module_demo

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.module_base.common.BaseActivity
import com.zp.module_base.common.BaseModel
import com.zp.module_base.common.BaseViewModelImpl
import com.zp.module_base.content.Routes
import com.zp.module_base.content.jumpActivity
import com.zp.module_base.util.L
import com.zp.module_demo.databinding.ActivityMainBinding
import com.zp.module_file.view.FileMainActivity
import com.zp.module_user.view.UserInfoActivity

@Route(path = Routes.APP_ROUTE_MAIN)
class MainActivity : BaseActivity<MainModel, ActivityMainBinding, MainViewModel>() {

    override fun getContentView() = R.layout.activity_main

    override fun getViewModelClass() = MainViewModel::class.java

    override fun getModel() = MainModel()

    override fun initAll(savedInstanceState: Bundle?) {
        setBarTitle("合并运行")
        bindView?.mainViewModel = viewModel
    }

}

class MainViewModel : BaseViewModelImpl<MainModel, String>() {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.main_userBtn -> {
                ARouter.getInstance().build(Routes.USER_ROUTE_INFO).navigation()
            }
            R.id.main_fileBtn -> {
                ARouter.getInstance().build(Routes.FILE_ROUTE_MAIN).navigation()
            }
            else -> L.e("GG")
        }
    }

}

class MainModel : BaseModel()


