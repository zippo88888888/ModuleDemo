package com.zp.module_base.util

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.zp.module_base.R
import java.io.File

object ImageLoader {

    /**
     * 加载图片
     */
    fun loadImage(res: Any, pic: ImageView, roundingRadius: Int = 0) {
        when (res) {
            is Int -> loadImageByRes(res, pic, roundingRadius)
            is File -> loadImageByFile(res, pic, roundingRadius)
            is Uri -> loadImageByUri(res, pic, roundingRadius)
            is String -> loadImageByNet(res, pic, roundingRadius)
            else -> L.e("图片类型错误")
        }
    }

    private fun loadImageByRes(res: Int, pic: ImageView, roundingRadius: Int = 0) {
        Glide.with(pic.context)
            .load(res)
            .apply(getOptions(roundingRadius))
            .into(pic)
    }

    private fun loadImageByFile(file: File, pic: ImageView, roundingRadius: Int = 0) {
        Glide.with(pic.context)
            .load(file)
            .apply(getOptions(roundingRadius))
            .into(pic)
    }

    private fun loadImageByUri(uri: Uri, pic: ImageView, roundingRadius: Int = 0) {
        Glide.with(pic.context)
            .load(uri)
            .apply(getOptions(roundingRadius))
            .into(pic)
    }

    private fun loadImageByNet(
        path: String,
        pic: ImageView,
        roundingRadius: Int = 0,
        placeholder: Int = R.drawable.ic_logo_g,
        error: Int = R.drawable.ic_logo_g
    ) {
        Glide.with(pic.context)
            .load(path)
            .apply(getOptions(roundingRadius, placeholder, error))
            .into(pic)
    }


    private fun getOptions(
        roundingRadius: Int = 0,
        placeholder: Int = R.drawable.ic_logo_g,
        error: Int = R.drawable.ic_logo_g
    ) =
        RequestOptions().apply {
            if (roundingRadius > 0) {
                bitmapTransform(RoundedCorners(roundingRadius))
            }
            placeholder(placeholder)
            error(error)
        }

}