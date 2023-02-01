package com.hloong.lib.longlog

import com.hloong.lib.longlog.base.LongLogConfig
import com.hloong.lib.longlog.base.LongLogPrinter
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingDeque

class LongFilePrinter(private var logPath: String, private var retentionTime: Long) :
    LongLogPrinter {
    private var writer: LogWriter

    @Volatile
    private var worker: PrintWorker

    init {
        writer = LogWriter()
        worker = PrintWorker()
        cleanExpireLog()
    }

    /**
     * 清除过期log
     */
    private fun cleanExpireLog() {
        if (retentionTime <= 0) {
            return
        }
        var currentTime = System.currentTimeMillis()
        var logDir = File(logPath)
        var files = logDir.listFiles() ?: return
        for (file in files) {
            if (currentTime - file.lastModified() > retentionTime) {
                file.delete()
            }
        }
    }

    override fun print(config: LongLogConfig, level: Int, tag: String?, msg: String) {
        var timeMillis = System.currentTimeMillis()
        if (!worker.isRunning()) {
            worker.start()
        }
        worker.put(LongLogMo(timeMillis, level, tag!!, msg))
    }

    private inner class PrintWorker : Runnable {
        private var logs: BlockingQueue<LongLogMo> = LinkedBlockingDeque()

        @Volatile
        private var running = false

        /**
         * 将log放入打印队列
         * @param log 要被打印的log
         */
        fun put(log: LongLogMo) {
            try {
                logs.put(log)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        /**
         * 判断工作线程是否还在运行中
         * @return true 在运行
         */
        fun isRunning(): Boolean {
            synchronized(this) { return running }
        }

        fun start() {
            synchronized(this) {
                EXECUTOR.execute(this)
                running = true
            }
        }

        override fun run() {
            var log: LongLogMo
            while (true) {
                try {
                    log = logs.take()
                    doPrint(log)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun doPrint(logMo: LongLogMo) {
        var lastFileName: String = writer.preFileName.toString()
        if (lastFileName == null) {
            var newFileName = genFileName()
            if (writer.isReady) {
                writer.close()
            }
            if (!writer.ready(newFileName)) {
                return
            }
        }
        writer.append(logMo.flattenedLog())
    }

    private fun genFileName(): String {
        var sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(System.currentTimeMillis()))
    }

    /**
     * 基于BufferedWriter将log写入文件
     */
    private inner class LogWriter {
        var preFileName: String? = null
            private set
        private var logFile: File? = null
        private var bufferedWriter: BufferedWriter? = null
        val isReady: Boolean
            get() = bufferedWriter != null

        /**
         * log写入前的准备操作
         * @param newFileName 要保存log的文件名
         * @return true 表示准备就绪
         */
        fun ready(newFileName: String?): Boolean {
            preFileName = newFileName
            logFile = File(logPath, newFileName)
            // 当log文件不存在时创建log文件
            if (!logFile!!.exists()) {
                try {
                    val parent = logFile!!.parentFile
                    if (!parent.exists()) {
                        parent.mkdirs()
                    }
                    logFile!!.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                    preFileName = null
                    logFile = null
                    return false
                }
            }
            try {
                bufferedWriter = BufferedWriter(FileWriter(logFile, true))
            } catch (e: Exception) {
                e.printStackTrace()
                preFileName = null
                logFile = null
                return false
            }
            return true
        }

        /**
         * 关闭bufferedWriter
         */
        fun close(): Boolean {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    return false
                } finally {
                    bufferedWriter = null
                    preFileName = null
                    logFile = null
                }
            }
            return true
        }

        /**
         * 将log写入文件
         * @param flattenedLog 格式化后的log
         */
        fun append(flattenedLog: String?) {
            try {
                if (bufferedWriter != null){
                    bufferedWriter!!.write(flattenedLog)
                    bufferedWriter!!.newLine()
                    bufferedWriter!!.flush()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private val EXECUTOR = Executors.newSingleThreadExecutor()
        private var instance: LongFilePrinter? = null
        @Synchronized
        fun getInstance(logPath: String, retentionTime: Long): LongFilePrinter? {
            if (instance == null) {
                instance = LongFilePrinter(logPath, retentionTime)
            }
            return instance
        }
    }
}