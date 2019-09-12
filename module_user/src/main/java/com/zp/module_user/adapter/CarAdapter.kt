package com.zp.module_user.adapter

import android.view.View
import android.widget.PopupMenu
import com.zp.module_base.bean.CarItemBean
import com.zp.module_base.common.BaseDBAdapter
import com.zp.module_base.common.SystemDialog
import com.zp.module_base.content.showToast
import com.zp.module_user.R
import com.zp.module_user.databinding.ItemCarListBinding

class CarAdapter : BaseDBAdapter<ItemCarListBinding, CarItemBean>(R.layout.item_car_list) {

    var changeEveryThingListener: ((Long, Boolean?) -> Unit)? = null

    override fun bindView(helper: DBHolder<ItemCarListBinding>, item: CarItemBean, position: Int) {
        helper.dataBinding?.run {
            itemCarBean = item
            executePendingBindings()
            itemCarListBox.setOnClickListener {
                changeEveryThingListener?.invoke(getSelectPrice(), checkSelectAll())
            }
            itemCarListCountView.shopCountAddClick = {
                item.count = it
                changeEveryThingListener?.invoke(getSelectPrice(), null)
            }
            itemCarListCountView.shopCountSubtractClick = {
                item.count = it
                changeEveryThingListener?.invoke(getSelectPrice(), null)
            }
            itemCarListDianPic.setOnClickListener {
                showMoreMenu(it)
            }
        }
    }

    /**
     * 获取选中的价格
     */
    fun getSelectPrice(): Long {
        var price = 0L
        getSelectData().forEach {
            price += it.price * it.count
        }
        return price
    }

    /**
     * 获取选中的数据
     */
    fun getSelectData() = data.filter { it.checked }

    /**
     * 检查是否全部选中
     */
    private fun checkSelectAll() = data.all {
        it.checked
    }

    /**
     * 全选或全不选
     */
    fun selectAllOrUnSelectAll(isSelectAll: Boolean) {
        data.forEach {
            it.checked = isSelectAll
        }
        notifyDataSetChanged()
    }

    /**
     * 删除选中的值
     */
    fun deleteSelecteData() {
        SystemDialog(mContext).showDialog2({
            if (data.removeAll(getSelectData())) {
                changeEveryThingListener?.invoke(0, false)
                notifyDataSetChanged()
            }
        }, {}, "您确定要删除吗？", "确定", "取消")
    }

    private fun showMoreMenu(v: View) {
        PopupMenu(mContext, v).apply {
            menuInflater.inflate(R.menu.menu_item_more, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_more_like -> showToast("已喜欢")
                    R.id.menu_item_more_collect -> showToast("已收藏")
                    R.id.menu_item_more_feedback -> showToast("已反馈")
                    else -> showToast("GG")
                }
                true
            }
            show()
        }
    }

}