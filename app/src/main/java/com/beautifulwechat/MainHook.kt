package com.beautifulwechat

import android.content.Context
import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

class MainHook : IXposedHookLoadPackage {
    
    companion object {
        private const val TAG = "BeautifulWeChat"
        private const val TARGET_PACKAGE = "com.tencent.mm"
        private const val LOG_DIR = "/sdcard/Android/data/com.beautifulwechat"
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != TARGET_PACKAGE) return

        try {
            logInfo("==========================================")
            logInfo("Beautiful WeChat Module Loaded")
            @Suppress("DEPRECATION")
            val versionCode = lpparam.appInfo.versionCode
            logInfo("WeChat Version: $versionCode")
            logInfo("==========================================")

            // 初始化 Hookers
            val classLoader = lpparam.classLoader
            
            // Hook 聊天气泡
            BubbleHooker.hook(classLoader)
            
            // Hook 顶部栏
            TopBarHooker.hook(classLoader)
            
            logInfo("All hooks initialized successfully")
            
        } catch (e: Throwable) {
            logError("Failed to initialize hooks", e)
        }
    }

    private fun logInfo(message: String) {
        Log.i(TAG, message)
        writeLog("INFO", message)
    }

    private fun logError(message: String, throwable: Throwable) {
        Log.e(TAG, message, throwable)
        writeLog("ERROR", "$message: ${throwable.message}")
    }

    private fun writeLog(level: String, message: String) {
        try {
            val logDir = File(LOG_DIR)
            if (!logDir.exists()) {
                logDir.mkdirs()
            }
            
            val logFile = File(logDir, "beautifulwechat.log")
            val timestamp = dateFormat.format(Date())
            val logMessage = "$timestamp [$level] $message\n"
            
            FileWriter(logFile, true).use { writer ->
                writer.write(logMessage)
            }
        } catch (e: Exception) {
            // 忽略日志写入错误
        }
    }
}
