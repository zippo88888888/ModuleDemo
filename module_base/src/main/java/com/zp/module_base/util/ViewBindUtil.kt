package com.zp.module_base.util

import android.widget.CompoundButton
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.zp.module_base.widget.ShopCountView

object ViewBindUtil {

    /**
     * 图片加载
     */
    @JvmStatic
    @BindingAdapter("android:bind_imageUrl")
    fun loadImageByXml(pic: ImageView, imageUrl: String) {
        ImageLoader.loadImage(imageUrl, pic)
    }

    /**
     * CheckBox RadioButton 只有值相同才赋值（设置状态）  完成单向绑定  1）
     *
     * https://juejin.im/post/5a55ecb6f265da3e4d7298e9#heading-15
     * https://www.jianshu.com/p/e8b6ba90de53
     * https://chrnie.com/2016/12/02/%E8%87%AA%E5%AE%9A%E4%B9%89-DataBinding-%E5%8F%8C%E5%90%91%E7%BB%91%E5%AE%9A%E5%B1%9E%E6%80%A7/
     */
    @JvmStatic
    @BindingAdapter("android:bind_isChecked")
    fun setCheckBoxStateByXml(box: CompoundButton, isChecked: Boolean) {
        if (box.isChecked != isChecked) {
            box.isChecked = isChecked
        }
    }

    /**
     *  CheckBox RadioButton 获取状态   2）
     */
    @JvmStatic
    @InverseBindingAdapter(attribute = "android:bind_isChecked", event = "thisBind_isCheckedChange")
    fun getCheckBoxStateByXml(box: CompoundButton) = box.isChecked

    /**
     * CheckBox RadioButton 设置监听 完成双向绑定 3）
     */
    @JvmStatic
    @BindingAdapter("thisBind_isCheckedChange")
    fun setCheckBoxStateByXmlListener(box: CompoundButton, listener: InverseBindingListener) {
        box.setOnCheckedChangeListener { _, _ ->
            listener.onChange()
        }
    }

    /**
     * ShopCountView 不采用双向绑定
     */
    @JvmStatic
    @BindingAdapter("android:bind_shopCount")
    fun setShopCountByXml(shopCountView: ShopCountView, count: Int) {
        shopCountView.setValue(count)
    }

    /*@JvmStatic
    @InverseBindingAdapter(attribute = "android:bind_shopCount", event = "thisBind_shopCountChange")
    fun getShopCountByXml(shopCountView: ShopCountView) = shopCountView.getValue() ?: 1

    @JvmStatic
    @BindingAdapter("thisBind_shopCountChange")
    fun setShopCountListenerByXml(shopCountView: ShopCountView, listener: InverseBindingListener) {
        shopCountView.shopCountAddClick = {
            listener.onChange()
        }
        shopCountView.shopCountSubtractClick = {
            listener.onChange()
        }
    }*/

}