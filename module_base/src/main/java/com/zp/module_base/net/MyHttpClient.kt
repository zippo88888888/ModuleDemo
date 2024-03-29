package com.zp.module_base.net

import androidx.collection.ArrayMap
import com.zp.module_base.content.IS_DEBUG
import com.zp.module_base.net.api.HttpApi
import com.zp.module_base.util.L
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MyHttpClient {

    /**
     * 缓存retrofit针对同一个域名下相同的ApiService不会重复创建retrofit对象
     */
    private val apiMap by lazy {
        ArrayMap<String, Any>()
    }

    private object Builder {
        val builder = MyHttpClient()
    }

    companion object {
        private const val API_KEY = "apiKey"

        fun getInstance() = Builder.builder
    }

    private var converterFactory: Converter.Factory? = null

    /**
     * 后台可能会返回 不符合标准的json字符串，需要重新设置
     */
    fun setConverterFactory(converterFactory: Converter.Factory): MyHttpClient {
        this.converterFactory = converterFactory
        return this
    }

    /**
     * 清空所有api缓存
     */
    fun clearAllApi() {
        apiMap.clear()
    }

    /**
     * 创建默认的Api
     */
    fun createDefaultApi() = createRetrofitApi(URL.ROOT_URL, HttpApi::class.java)

    /**
     * 根据 apiClass 与 baseUrl 创建 不同的Api
     * @param baseUrl String    根目录
     * @param clazz Class<T>    具体的api 提示
     * @param showLog Boolean   是否需要显示log（文件上传、下载不需要显示log）
     */
    fun <T> createRetrofitApi(baseUrl: String, clazz: Class<T>, showLog: Boolean = IS_DEBUG): T {
        val key = getApiKey(baseUrl, clazz)
        val api = apiMap[key] as T
        if (api == null) {
            L.e(API_KEY, "Hint --->>> \"$key\"不存在，需要创建新的")
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
//                    .addInterceptor(HeaderInterceptor()) // 头部拦截器
            if (showLog) {
                builder.addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                    L.i("http", it)
                }
                ).apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(builder.build())
                .addConverterFactory(converterFactory ?: GsonConverterFactory.create())
                .build()
            val newAapi = retrofit.create(clazz)
            apiMap[key] = newAapi
            return newAapi
        }
        L.i(API_KEY, "\"$key\"已经存在，直接使用")
        return api
    }

    private fun <K> getApiKey(baseUrl: String, apiClass: Class<K>) =
            "apiKey_${baseUrl}_${apiClass.name}"

}