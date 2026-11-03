package com.beautifulwechat.hook

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.beautifulwechat.util.Logger
import io.github.libxposed.api.XposedModule

class TopBarHookStrategy(
    private val module: XposedModule,
    private val classLoader: ClassLoader
) {
    
    companion object {
        // 顶栏相关类
        private val TOPBAR_CLASS_CANDIDATES = arrayOf(
            "com.tencent.mm.ui.chatting.ChattingUI",
            "com.tencent.mm.ui.chatting.ChattingUIActivity",
            "androidx.appcompat.app.ActionBar",
            "android.app.ActionBar"
        )
        
        // 半透明黑色背景
        private val TOPBAR_BACKGROUND = ColorDrawable(Color.argb(128, 0, 0, 0))
    }
    
    fun install() {
        // 尝试Hook ActionBar的背景设置
        hookActionBar()
        
        // 尝试Hook微信聊天页面的顶栏
        hookChattingUI()
    }
    
    private fun hookActionBar() {
        try {
            val actionBarClass = classLoader.loadClass("android.app.ActionBar")
            val method = actionBarClass.getDeclaredMethod(
                "setBackgroundDrawable",
                android.graphics.drawable.Drawable::class.java
            )
            
            module.hook(method).before { param ->
                try {
                    // 替换为半透明背景
                    param.args[0] = TOPBAR_BACKGROUND
                } catch (e: Exception) {
                    Logger.error("event=hook_execution_failed method=setBackgroundDrawable", e)
                }
            }
            
            Logger.info("event=hook_registered method=ActionBar.setBackgroundDrawable")
            
        } catch (e: ClassNotFoundException) {
            Logger.warn("event=class_not_found class=android.app.ActionBar")
        } catch (e: NoSuchMethodException) {
            Logger.warn("event=method_not_found method=setBackgroundDrawable")
        } catch (e: Exception) {
            Logger.error("event=hook_failed method=ActionBar.setBackgroundDrawable", e)
        }
    }
    
    private fun hookChattingUI() {
        for (className in TOPBAR_CLASS_CANDIDATES) {
            try {
                val clazz = classLoader.loadClass(className)
                hookChattingUIClass(clazz)
                Logger.info("event=topbar_class_found class=$className")
                return
            } catch (e: ClassNotFoundException) {
                // 继续尝试
            }
        }
        
        Logger.warn("event=topbar_class_not_found candidates=${TOPBAR_CLASS_CANDIDATES.size}")
    }
    
    private fun hookChattingUIClass(targetClass: Class<*>) {
        try {
            // Hook onCreate 方法
            val method = targetClass.getDeclaredMethod("onCreate", android.os.Bundle::class.java)
            
            module.hook(method).after { param ->
                try {
                    // 在onCreate后设置ActionBar背景
                    val activity = param.thisObject as? android.app.Activity
                    activity?.actionBar?.setBackgroundDrawable(TOPBAR_BACKGROUND)
                } catch (e: Exception) {
                    Logger.error("event=hook_execution_failed method=onCreate", e)
                }
            }
            
            Logger.info("event=hook_registered method=ChattingUI.onCreate")
            
        } catch (e: NoSuchMethodException) {
            Logger.warn("event=method_not_found method=onCreate")
        } catch (e: Exception) {
            Logger.error("event=hook_failed method=onCreate", e)
        }
    }
}
