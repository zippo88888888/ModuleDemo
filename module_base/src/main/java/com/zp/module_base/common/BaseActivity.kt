package com.zp.module_base.common

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.appbar.AppBarLayout
import com.zp.module_base.content.IS_DEBUG
import com.zp.module_base.content.getColorById
import com.zp.module_base.content.getStatusBarHeight
import com.zp.module_base.content.getTextValue
import kotlinx.android.synthetic.main.layout_tool_bar.*
import java.lang.Exception
import java.lang.NullPointerException

/**
 * 基于 AndroidX + LiveData + ViewModel + DataBinding + Retrofit + Kotlin + 协程 + 组件化 的 MVVM
 */
abstract class BaseActivity<M : BaseModel, V : ViewDataBinding, VM : BaseViewModel<M>> : AppCompatActivity(),
    Toolbar.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener
    , BaseViewModelImpl.ILiveDataObserveListener {

    protected var bindView: V? = null
    protected var viewModel: VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = getContentView()
        if (contentView <= 0) throw NullPointerException("Activity ContentView Is Not Null")
        bindView = DataBindingUtil.setContentView(this, getContentView())
//        viewModel = ViewModelProvider.NewInstanceFactory().create(getViewModelClass())
        if (getViewModelClass() != null) {
            viewModel = ViewModelProviders.of(this).get(getViewModelClass()!!)
            viewModel?.attachModel(getModel())
        }
//        ARouter.getInstance().inject(this)
        if (getBarState()) {
            initBar()
        }
        initAll(savedInstanceState)
        if (needLiveDataObserveInThis()) {
            liveDataObserveThis()
        }
    }

    protected open fun getBarState() = true

    abstract fun getContentView(): Int

    abstract fun getViewModelClass(): Class<VM>?

    abstract fun getModel(): M?

    abstract fun initAll(savedInstanceState: Bundle?)

    protected open fun needLiveDataObserveInThis() = false

    private fun liveDataObserveThis() {
        try {
            (viewModel as BaseViewModelImpl<*, *>).apply {
                noAnyData.observe(this@BaseActivity, Observer {
                    if (it) noAnyData()
                })
                noLoadMoreData.observe(this@BaseActivity, Observer {
                    if (it) noLoadMoreData()
                })
                refreshDataError.observe(this@BaseActivity, Observer {
                    if (it) refreshDataError()
                })
                loadMoreDataError.observe(this@BaseActivity, Observer {
                    if (it) loadMoreDataError()
                })
            }
        } catch (e: Exception) {
            if (IS_DEBUG) e.printStackTrace()
        }
    }

    override fun onRefresh() = Unit

    open fun onLoadMore() = Unit

    /**
     * 没有任何数据
     */
    override fun noAnyData() = Unit

    /**
     * 没有加载更多的数据了
     */
    override fun noLoadMoreData() = Unit

    /**
     * 刷新或请求数据出错
     */
    override fun refreshDataError() = Unit

    /**
     * 加载更多数据出错
     */
    override fun loadMoreDataError() = Unit

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewModel?.onNewIntent(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    final override fun onLoadMoreRequested() {
        onLoadMore()
    }

    protected open fun back() {
        onBackPressed()
    }

    override fun onMenuItemClick(item: MenuItem?) = true

    protected fun createMenu(menuLayout: Int, menu: Menu?): Boolean {
        menuInflater.inflate(menuLayout, menu)
        return true
    }

    private fun initBar() {
        tool_bar_statusView.layoutParams = AppBarLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()
        )
        setSupportActionBar(tool_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setNavigationClick()
    }

    protected fun setNavigationIcon(icon: Int) {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        tool_bar.setNavigationIcon(icon)
        setNavigationClick()
    }

    protected fun setBarTitle(title: Any) {
        supportActionBar?.title = ""
        tool_bar_title.text = getTextValue(title)
    }

    protected fun setBarColor(color: Int) {
        tool_app_barLayout.setBackgroundColor(getColorById(color))
    }

    protected fun setNavigationClick() {
        tool_bar.setNavigationOnClickListener { back() }
    }

    protected fun getToolBar(): Toolbar? = tool_bar

    protected fun setOnMenuItemClickListener() {
        tool_bar.setOnMenuItemClickListener(this)
    }

}