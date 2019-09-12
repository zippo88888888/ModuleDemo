package com.zp.module_base.net.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * 文件相关的api
 */
interface FileApi {


    /**
     * 使用 LiveData 或者 挂起函数、协程
     * https://johnnyshieh.me/posts/kotlin-coroutine-introduction/
     */
    @Streaming
    @GET
    fun downloadFile(@Url dowloadUrl: String): Call<ResponseBody>

}