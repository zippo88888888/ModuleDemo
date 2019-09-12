package com.zp.module_base.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.zp.module_base.content.IS_DEBUG
import com.zp.module_base.content.getAppContext
import com.zp.module_base.content.getJob
import com.zp.module_base.content.runUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object MyFileUtil {

    /** 文件大小单位为B */
    const val SIZETYPE_B = 1
    /** 文件大小单位为KB */
    const val SIZETYPE_KB = 2
    /** 文件大小单位为MB */
    const val SIZETYPE_MB = 3
    /** 文件大小单位为GB */
    const val SIZETYPE_GB = 4

    /** 根目录 */
    private const val ROOT_NAME = "zp_shop_mall"

    /** 照片目录 */
    const val PHOTO = "/photo/"
    /** 视频目录 */
    const val VIDEO = "/video/"
    /** 音频目录 */
    const val SOUND = "/sound/"
    /** 其他目录 */
    const val OTHERS = "/other/"

    /** Glide造成的磁盘缓存大小 */
    private val GLIDE_DISK_CACHE by lazy {
        "${getAppContext().cacheDir}/${InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR}"
    }

    const val SAVE_TO_LOCAL_PIC_NAME = ROOT_NAME

    /** 保存至本地相册的路径 */
    private val MY_DOWLOAD by lazy {
        Environment.getExternalStorageDirectory().absolutePath + "/DCIM.zp_shop_mall/"
    }

    /** 缓存列表 */
    private fun getCacheList() = arrayOf(
            getPath() + PHOTO,
            getPath() + VIDEO,
            getPath() + SOUND,
            getPath() + OTHERS,
            GLIDE_DISK_CACHE
    )

    private var storagePath: String? = null
    private var packageFilesDirectory: String? = null

    /**
     * 得到具体的路径
     */
    fun getPathForPath(path: String): String {
        val url = getPath() + path
        val file = File(url)
        if (!file.exists()) {
            file.mkdirs()
        }
        return url
    }


    private fun getPath(context: Context = getAppContext()): String? {
        if (storagePath == null) {
            storagePath = context.getExternalFilesDir(null)?.path + "/" + ROOT_NAME
            val file = File(storagePath)
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    storagePath =
                        getPathInPackage(context, true)
                }
            }
        }
        return storagePath
    }

    private fun getPathInPackage(context: Context, grantPermissions: Boolean): String? {
        if (packageFilesDirectory != null) return packageFilesDirectory
        // 手机不存在sdcard, 需要使用 data/data/name.of.package/files 目录
        val path = "${context.filesDir}/$ROOT_NAME"
        val file = File(path)
        if (!file.exists()) {
            if (!file.mkdirs()) {
                L.e("在pakage目录创建CGE临时目录失败!")
                return null
            }
            if (grantPermissions) { // 设置隐藏目录权限.
                if (file.setExecutable(true, false)) L.e("文件可执行")
                if (file.setReadable(true, false)) L.e("文件可读")
                if (file.setWritable(true, false)) L.e("文件可写")
            }
        }
        packageFilesDirectory = path
        return packageFilesDirectory
    }

    /**
     * 根据当前时间 + 后缀 命名
     * @param suffix 后缀
     * @param index  下标
     */
    fun getFileName(suffix: String, index: Int = -1): String {
        val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA)
        val timeStamp = simpleDateFormat.format(Date())
        return if (index > 0) timeStamp + "_$index" + suffix else timeStamp + suffix
    }

    /**
     * 获取指定文件或文件夹的指定单位的大小
     * @param filePath 文件或文件夹路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     */
    fun getFileOrFilesSize(filePath: String, sizeType: Int = SIZETYPE_MB): Double {
        val file = File(filePath)
        var blockSize: Long = 0
        try {
            blockSize = if (file.isDirectory) getFileSizes(file)
            else getFileSize(file)
        } catch (e: Exception) {
            if (IS_DEBUG) e.printStackTrace()
            L.e("获取文件大小--->>>获取失败!")
        } finally {
            return formetFileSize(blockSize, sizeType)
        }
    }

    /**
     * 获取指定文件大小
     */
    private fun getFileSize(file: File) = if (file.exists()) file.length() else 0L

    /**
     * 获取指定文件夹
     */
    private fun getFileSizes(f: File): Long {
        var size: Long = 0
        val flist = f.listFiles()
        for (i in flist!!.indices) {
            size = if (flist[i].isDirectory) {
                size + getFileSizes(flist[i])
            } else {
                size + getFileSize(flist[i])
            }
        }
        return size
    }

    /**
     * 转换文件大小,指定转换的类型
     */
    fun formetFileSize(fileS: Long, sizeType: Int = SIZETYPE_MB): Double {
        val df = DecimalFormat("#.00")
        return when (sizeType) {
            SIZETYPE_B -> java.lang.Double.valueOf(df.format(fileS.toDouble()))
            SIZETYPE_KB -> java.lang.Double.valueOf(df.format(fileS.toDouble() / 1024))
            SIZETYPE_MB -> java.lang.Double.valueOf(df.format(fileS.toDouble() / 1048576))
            SIZETYPE_GB -> java.lang.Double.valueOf(df.format(fileS.toDouble() / 1073741824))
            else -> 0.0
        }
    }

    private fun checkDowloadUrl() {
        File(MY_DOWLOAD).apply {
            if (!exists()) mkdirs()
        }
    }

    /**
     * 保存图片至系统相册
     */
    fun savePicToSystemPhoto(context: Context, picFile: File): Boolean {
        checkDowloadUrl()
        val newName = SAVE_TO_LOCAL_PIC_NAME + picFile.name
        val newFile = File(MY_DOWLOAD + newName)
        return if (copyFile(picFile, newFile)) {
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile)))
            L.e("图片已保存至本地相册")
            true
        } else false
    }

    /**
     * 保存视频至系统相册
     */
    fun saveVideoToSystemPhoto(context: Context, videoPath: String, duration: Int = 0): Boolean {
        val oldFile = File(videoPath)
        checkDowloadUrl()
        val newFilePath = "$MY_DOWLOAD/rx_t_${oldFile.name}"
        val newFile = File(newFilePath)
        if (copyFile(oldFile, newFile)) {
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = Uri.fromFile(File(newFilePath))
            context.sendBroadcast(intent)
            L.e("视频已保存至本地相册")
            return true
        }
        return false
    }

    /**
     * 复制文件
     * @param sourceFile    原文件
     * @param targetFile    复制的文件
     */
    fun copyFile(sourceFile: File, targetFile: File): Boolean {
        var success = false
        // 新建文件输入流并对它进行缓冲
        val input = FileInputStream(sourceFile)
        val inBuff = BufferedInputStream(input)
        // 新建文件输出流并对它进行缓冲
        val output = FileOutputStream(targetFile)
        val outBuff = BufferedOutputStream(output)
        try {
            // 缓冲数组
            val b = ByteArray(1024 * 5)
            while (true) {
                val len = inBuff.read(b)
                if (len == -1) {
                    break
                } else {
                    outBuff.write(b, 0, len)
                }
            }
            // 刷新此缓冲的输出流
            outBuff.flush()
            success = true
        } catch (e: Exception) {
            if (IS_DEBUG) e.printStackTrace()
            success = false
        } finally {
            //关闭流
            inBuff.close()
            outBuff.close()
            output.close()
            input.close()
            return success
        }
    }

    /**
     * 删除文件
     */
    fun delete(file: File) {
        if (!file.exists()) return
        if (file.isFile) {
            file.delete()
            return
        }
        if (file.isDirectory) {
            val childFiles = file.listFiles()
            if (childFiles == null || childFiles.isEmpty()) {
                file.delete()
                return
            }
            for (i in childFiles.indices) {
                delete(childFiles[i])
            }
            file.delete()
        }
    }

    /**
     * 获取手机可用空间
     *
     * 获取内存可用剩余空间 Environment.getDataDirectory().freeSpace
     * 获取SD卡可用剩余空间 Environment.getExternalStorageDirectory().freeSpace
     */
    fun getFreeSpace() = formetFileSize(Environment.getExternalStorageDirectory().freeSpace).run {
        L.e("sd卡可用空间--->>> $this MB")
        this
    }


    /**
     * 获取缓存大小
     * @return Job      方便调用者及时取消
     */
    fun getCacheSize(sizeType: Int = SIZETYPE_MB, block: (Double) -> Unit) = getJob {
        var size = 0.0
        getCacheList().forEach {
            if (File(it).exists()) {
                val filesSize = getFileOrFilesSize(it, sizeType)
                L.e("$it 大小 $filesSize")
                size += filesSize
            }
        }
        runUI {
            block(size)
        }
    }

    /**
     * 获取缓存目录的大小
     */
    private suspend fun callCacheSize(sizeType: Int = SIZETYPE_MB) =
        withContext(Dispatchers.IO) {
            var size = 0.0
            getCacheList().forEach {
                if (File(it).exists()) {
                    val filesSize = getFileOrFilesSize(it, sizeType)
                    L.e("$it 大小 $filesSize")
                    size += filesSize
                }
            }
            size
        }

    /**
     * 清除缓存
     * @return Job      方便调用者及时取消
     */
    fun deleteCache(block: () -> Unit) = getJob {
        getCacheList().forEach {
            if (it == GLIDE_DISK_CACHE) {
                Glide.get(getAppContext()).clearDiskCache()
            } else {
                delete(File(it))
            }
        }
        runUI {
            block()
        }
    }

}