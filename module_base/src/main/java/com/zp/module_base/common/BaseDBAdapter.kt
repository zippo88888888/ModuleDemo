package com.zp.module_base.common

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zp.module_base.R
import com.zp.module_base.util.L

abstract class BaseDBAdapter<VDB : ViewDataBinding, T>(layoutId: Int) :
    BaseQuickAdapter<T, BaseDBAdapter.DBHolder<VDB>>(layoutId) {

    var itemClickListener: ((VDB?, T, Int) -> Unit)? = null

    final override fun convert(helper: DBHolder<VDB>?, item: T) {
        if (helper == null) L.e("GG")
        else {
            val position = helper.layoutPosition
            helper.itemView.setOnClickListener {
                itemClickListener?.invoke(helper.dataBinding, item, position)
            }
            bindView(helper, item, position)
        }
    }

    open fun getDataSize() = data.size

    abstract fun bindView(helper: DBHolder<VDB>, item: T, position: Int)

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View {
        val bindView = DataBindingUtil.inflate<VDB>(mLayoutInflater, layoutResId, parent, false)
            ?: return super.getItemView(layoutResId, parent)
        return bindView.root.run {
            setTag(R.id.BaseDBAdapter_DataBinding_Tag, bindView)
            this
        }
    }

    class DBHolder<VDB : ViewDataBinding>(rootView: View) : BaseViewHolder(rootView) {

        val dataBinding by lazy {
//            itemView.getTag(R.id.BaseDBAdapter_DataBinding_Tag) as VDB
            DataBindingUtil.getBinding<VDB>(itemView)
        }
    }

}