package com.zp.module_base.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.lang.NullPointerException
import java.util.ArrayList

// TODO 自己封装的
abstract class DataBindingAdapter<VDB : ViewDataBinding, T>(layoutId: Int) :
    RecyclerView.Adapter<DataBindingAdapter.DataBindingViewHolder>() {

    var itemClickListener: ((VDB?, T, Int) -> Unit)? = null

    private var layoutId = -1
    var datas: MutableList<T> = ArrayList()
        set(value) {
            datas.clear()
            field = value
            notifyDataSetChanged()
        }

    init {
        this.layoutId = layoutId
    }

    constructor(layoutId: Int, data: MutableList<T>) : this(layoutId) {
        this.datas = data
    }

    open fun setData(data: List<T>) {
        this.datas = datas
    }

    open fun addData(data: List<T>) {
        if (this.datas.addAll(data)) {
            notifyDataSetChanged()
        }
    }

    open fun addItem(t: T, position: Int = 0) {
        datas.add(position, t)
        notifyItemInserted(position)
    }

    open fun setItem(t: T, position: Int) {
        datas[position] = t
        notifyItemChanged(position)
    }

    open fun remove(position: Int) {
        datas.removeAt(position)
        notifyItemRemoved(position)
    }

    open fun clear(changeDataNow: Boolean = true) {
        datas.clear()
        if (changeDataNow) {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        val layoutId = getLayoutId(viewType)
        if (layoutId <= 0) throw NullPointerException("Adapter Layout Is Not Null")
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindView = DataBindingUtil.inflate<VDB>(layoutInflater, layoutId, parent, false)
        return DataBindingViewHolder(bindView.root)
    }

    final override fun getItemCount() = datas.size

    fun getItem(position: Int) = datas[position]

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        val bindView = DataBindingUtil.getBinding<VDB>(holder.itemView)
        holder.itemView.setOnClickListener { itemClickListener?.invoke(bindView, getItem(position), position) }
        bindData(bindView, getItem(position), position)
    }

    open fun getLayoutId(viewType: Int) = layoutId

    abstract fun bindData(bindView: VDB?, itemData: T, position: Int)

    class DataBindingViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)

}
