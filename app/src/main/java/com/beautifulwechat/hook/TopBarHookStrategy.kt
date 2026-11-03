package com.beautifulwechat.hook

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.beautifulwechat.util.Logger
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedInterface.BeforeHookCallback
import io.github.libxposed.api.XposedInterface.AfterHookCallback

class TopBarHookStrategy(
    private val module: XposedModule,
    private val classLoader: ClassLoader
) {
    
    companion object {
        private val TOPBAR_CLASS_CANDIDATES = arrayOf(
            "com.tencent.mm.ui.chatting.ChattingUI",
            "com.tencent.mm.ui.chatting.ChattingUIActivity",
            "androidx.appcompat.app.ActionBar",
            "android.app.ActionBar"
        )
        
        private val TOPBAR_BACKGROUND = ColorDrawable(Color.argb(128, 0, 0, 0))
    }
    
    fun install() {
        hookActionBar()
        hookChattingUI()
    }
    
    private fun hookActionBar() {
        try {
            val actionBarClass = classLoader.loadClass("android.app.ActionBar")
            val method = actionBarClass.getDeclaredMethod(
                "setBackgroundDrawable",
                android.graphics.drawable.Drawable::class.java
            )
            
            module.hook(method, object : BeforeHookCallback {
                override fun beforeHookedMethod(callback: XposedInterface.MethodHookCallback) {
                    try {
                        callback.args[0] = TOPBAR_BACKGROUND
                    } catch (e: Exception) {
                        Logger.error("event=hook_execution_failed method=setBackgroundDrawable", e)
                    }
                }
            })
            
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
                // 继续
            }
        }
        
        Logger.warn("event=topbar_class_not_found candidates=${TOPBAR_CLASS_CANDIDATES.size}")
    }
    
    private fun hookChattingUIClass(targetClass: Class<*>) {
        try {
            val method = targetClass.getDeclaredMethod("onCreate", android.os.Bundle::class.java)
            
            module.hook(method, object : AfterHookCallback {
                override fun afterHookedMethod(callback: XposedInterface.MethodHookCallback) {
                    try {
                        val activity = callback.thisObject as? android.app.Activity
                        activity?.actionBar?.setBackgroundDrawable(TOPBAR_BACKGROUND)
                    } catch (e: Exception) {
                        Logger.error("event=hook_execution_failed method=onCreate", e)
                    }
                }
            })
            
            Logger.info("event=hook_registered method=ChattingUI.onCreate")
            
        } catch (e: NoSuchMethodException) {
            Logger.warn("event=method_not_found method=onCreate")
        } catch (e: Exception) {
            Logger.error("event=hook_failed method=onCreate", e)
        }
    }
}
