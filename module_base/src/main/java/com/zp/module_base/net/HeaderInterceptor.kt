package com.zp.module_base.net

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    private val HTTP_TAG = "http"

    override fun intercept(chain: Interceptor.Chain): Response {
        /*val token = SPUtil.get("token", "") as String
        if (token.isNotEmpty()) { // 可以在这里更换 请求路径
            val newBuilder = request.newBuilder()
            newBuilder.addHeader("token", token)
            L.i(HTTP_TAG, token)
            return chain.proceed(newBuilder.build())
        }*/
        return chain.proceed(chain.request())
    }


}