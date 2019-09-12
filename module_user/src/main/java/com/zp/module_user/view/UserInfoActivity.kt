package com.zp.module_user.view

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.module_base.common.BaseActivity
import com.zp.module_base.content.Routes
import com.zp.module_base.content.jumpActivity
import com.zp.module_user.R
import com.zp.module_user.databinding.ActivityUserInfoBinding
import com.zp.module_user.model.UserInfoModel
import com.zp.module_user.view_model.UserInfoViewModel
import kotlinx.android.synthetic.main.activity_user_info.*

@Route(path = Routes.USER_ROUTE_INFO)
class UserInfoActivity : BaseActivity<UserInfoModel, ActivityUserInfoBinding, UserInfoViewModel>() {

    override fun getContentView() = R.layout.activity_user_info

    override fun getViewModelClass() = UserInfoViewModel::class.java

    override fun getModel() = UserInfoModel()

    override fun initAll(savedInstanceState: Bundle?) {
        setBarTitle("用户基本信息")
        user_info_titleTxt.setOnClickListener { jumpActivity(CarActivity::class.java) }
        user_info_fileBtn.setOnClickListener {
            ARouter.getInstance().build(Routes.FILE_ROUTE_MAIN).navigation()
        }
    }

}
