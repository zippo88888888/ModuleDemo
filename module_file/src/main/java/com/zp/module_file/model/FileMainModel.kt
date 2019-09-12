package com.zp.module_file.model

import com.zp.module_base.common.BaseModel
import com.zp.module_base.content.runUI
import com.zp.module_base.net.MyHttpClient
import com.zp.module_base.net.URL
import com.zp.module_base.net.api.FileApi
import okhttp3.ResponseBody
import java.io.*

typealias DownloadBlock = (Int, Long, Long) -> Unit

class FileMainModel : BaseModel() {

    fun downloadFile() =
        MyHttpClient.getInstance()
            .createRetrofitApi(URL.ROOT_URL, FileApi::class.java, false)
            .downloadFile(URL.TEST_DOWLOAD_URL)

    /**
     * 下载文件
     * @param body ResponseBody     文件Body
     * @param fileDir String        本地文件夹名称
     * @param fileName String       文件名称
     */
    suspend fun startDownloadFile(
        body: ResponseBody,
        fileDir: String,
        fileName: String,
        block: DownloadBlock
    ) {
        val file = fileDir + fileName
        val dowloadFile = File(file)
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            val fileReader = ByteArray(4096)
            // 文件总大小
            val fileSize = body.contentLength()
            // 当前已下载的大小
            var fileSizeDownloaded = 0L

            inputStream = body.byteStream()
            outputStream = FileOutputStream(dowloadFile)
            while (true) {
                val read = inputStream!!.read(fileReader)
                if (read == -1) {
                    // 下载完成
                    runUI {
                        block.invoke(0, fileSizeDownloaded, fileSize)
                    }
                    break
                }
                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read.toLong()
                runUI {
                    block.invoke(1, fileSizeDownloaded, fileSize)
                }
            }
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            runUI {
                block.invoke(-1, 0L, 0L)
            }
        } finally {
            outputStream?.close()
            inputStream?.close()
        }

    }

}