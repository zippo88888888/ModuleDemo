package com.zp.module_base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.zp.module_base.R
import com.zp.module_base.content.showToast
import kotlinx.android.synthetic.main.layout_shop_count.view.*

class ShopCountView : LinearLayout, View.OnClickListener {

    private var maxValue = 999
    private var value = 1

    var shopCountValueClick: ((Int) -> Unit)? = null
    var shopCountAddClick: ((Int) -> Unit)? = null
    var shopCountSubtractClick: ((Int) -> Unit)? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val array = context!!.obtainStyledAttributes(attrs, R.styleable.ShopCountView)
        value = array.getInteger(R.styleable.ShopCountView_countValue, 1)
        array.recycle()
        initView()
    }

    private fun initView() {
        inflate(context!!, R.layout.layout_shop_count, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        shop_count_addTxt.setOnClickListener(this)
        shop_count_subtractTxt.setOnClickListener(this)
        shop_count_valueTxt.setOnClickListener(this)
        shop_count_valueTxt.text = "$value"
    }

    fun setMaxValue(maxValue: Int): ShopCountView {
        this.maxValue = maxValue
        return this
    }

    fun setValue(value: Int) {
        if (value <= maxValue) {
            shop_count_valueTxt.text = value.toString()
        } else {
            shop_count_valueTxt.text = maxValue.toString()
        }
    }

    fun getValue(): Int? = shop_count_valueTxt.text.toString().toInt()

    override fun onClick(v: View?) {
        val value = shop_count_valueTxt.text.toString().toInt()
        when (v) {
            shop_count_addTxt -> {
                if (value + 1 <= maxValue) {
                    shop_count_valueTxt.text = (value + 1).toString()
                } else {
                    showToast("已超出最大值")
                    shop_count_valueTxt.text = maxValue.toString()
                }
                shopCountAddClick?.invoke(shop_count_valueTxt.text.toString().toInt())
            }
            shop_count_subtractTxt -> {
                if (value - 1 >= 1) {
                    shop_count_valueTxt.text = (value - 1).toString()
                } else {
                    showToast("数量最小为1")
                    shop_count_valueTxt.text = "1"
                }
                shopCountSubtractClick?.invoke(shop_count_valueTxt.text.toString().toInt())
            }
            shop_count_valueTxt -> {
                shopCountValueClick?.invoke(value)
            }
        }
    }

}