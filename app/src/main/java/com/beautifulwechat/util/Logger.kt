package com.beautifulwechat.util

import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object Logger {
    private const val TAG = "BeautifulWeChat"
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
    
    // 日志文件路径
    private const val LOG_DIR = "/sdcard/Android/data/com.beautifulwechat"
    private const val LOG_FILE = "beautifulwechat.log"
    
    // 日志级别
    private const val LEVEL_INFO = "INFO"
    private const val LEVEL_WARN = "WARN"
    private const val LEVEL_ERROR = "ERROR"

    fun info(message: String) {
        Log.i(TAG, message)
        writeLog(LEVEL_INFO, message, null)
    }

    fun warn(message: String) {
        Log.w(TAG, message)
        writeLog(LEVEL_WARN, message, null)
    }

    fun error(message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Log.e(TAG, message, throwable)
        } else {
            Log.e(TAG, message)
        }
        writeLog(LEVEL_ERROR, message, throwable)
    }

    private fun writeLog(level: String, message: String, throwable: Throwable?) {
        try {
            val logDir = File(LOG_DIR)
            if (!logDir.exists()) {
                logDir.mkdirs()
            }
            
            val logFile = File(logDir, LOG_FILE)
            val timestamp = dateFormat.format(Date())
            
            val logMessage = buildString {
                append("$timestamp [$level] $message")
                if (throwable != null) {
                    append("\n")
                    append(throwable.stackTraceToString())
                }
                append("\n")
            }
            
            FileWriter(logFile, true).use { writer ->
                writer.write(logMessage)
            }
        } catch (e: Exception) {
            // 静默忽略日志写入失败
        }
    }
}
