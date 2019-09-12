package com.zp.module_user.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.zp.module_base.common.BaseActivity
import com.zp.module_base.content.Routes
import com.zp.module_base.content.defaultObserve
import com.zp.module_base.content.getColorById
import com.zp.module_base.content.showToast
import com.zp.module_base.util.L
import com.zp.module_base.widget.RecyclerViewDivider
import com.zp.module_user.R
import com.zp.module_user.adapter.CarAdapter
import com.zp.module_user.databinding.ActivityCarBinding
import com.zp.module_user.model.CarModel
import com.zp.module_user.view_model.CarViewModel

@Route(path = Routes.USER_ROUTE_CAR)
class CarActivity : BaseActivity<CarModel, ActivityCarBinding, CarViewModel>() {

    private lateinit var carAdapter: CarAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_common, menu)
        menu?.findItem(R.id.menu_common_id)?.title = "编辑"
        return true
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        getToolBar()?.menu?.findItem(R.id.menu_common_id)?.title = viewModel?.getDelState()
        return super.onMenuItemClick(item)
    }

    override fun getContentView() = R.layout.activity_car

    override fun getViewModelClass() = CarViewModel::class.java

    override fun getModel() = CarModel()

    override fun needLiveDataObserveInThis() = true

    override fun initAll(savedInstanceState: Bundle?) {
        setBarTitle("购物车")
        setOnMenuItemClickListener()

        bindView?.carViewModel = viewModel?.run {
            defaultObserve(this@CarActivity) {
                bindView?.listRefreshaLayout?.isRefreshing = false
                carAdapter.setNewData(it)
            }
            loadMoreLiveData.observe(this@CarActivity, Observer {
                carAdapter.addData(it)
                carAdapter.loadMoreComplete()
            })
            isSelectAllState.observe(this@CarActivity, Observer {
                carAdapter.selectAllOrUnSelectAll(it)
                bindView?.carCountPriceTxt?.text = "￥${carAdapter.getSelectPrice()}"
            })
            isDelState.observe(this@CarActivity, Observer {
                bindView?.isDelState = it
            })

            this
        }
        bindView?.isDelState = false

        initRecyclerView()
        bindView?.listRefreshaLayout?.isRefreshing = true
        viewModel?.onRefresh()
    }

    private fun initRecyclerView() {
        bindView?.listRefreshaLayout?.apply {
            setColorSchemeColors(getColorById(R.color.baseColor))
            setOnRefreshListener(this@CarActivity)
        }
        carAdapter = CarAdapter()
        bindView?.listRecyclerView?.apply {
            addItemDecoration(RecyclerViewDivider.getDefaultDivider(this@CarActivity))
            layoutManager = LinearLayoutManager(this@CarActivity)
            adapter = carAdapter
        }
        carAdapter.apply {
            itemClickListener = { _, bean, _ -> showToast("查看商品详情$bean") }
            changeEveryThingListener = { price, isSelectAll ->
                bindView?.carSelectAllBox?.isChecked = isSelectAll ?: bindView!!.carSelectAllBox.isChecked
                bindView?.carCountPriceTxt?.text = "￥$price"
            }
//            setOnLoadMoreListener(this@ListActivity, bindView?.listRecyclerView)
        }
        viewModel?.bindCarAdapter(carAdapter)
    }

    override fun onRefresh() {
        viewModel?.onRefresh()
    }

    override fun onLoadMore() {
        viewModel?.onLoadMore()
    }

    override fun noAnyData() {
        L.e("测试没有任何数据，其实有数据！！！")
    }

    override fun noLoadMoreData() {
        carAdapter.loadMoreEnd()
    }

}
