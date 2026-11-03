package com.beautifulwechat

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.graphics.drawable.ColorDrawable
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_MethodHook

object TopBarHooker {
    
    private const val TAG = "TopBarHooker"
    
    // 微信顶栏相关类名
    private val TOPBAR_CLASS_NAMES = arrayOf(
        "com.tencent.mm.ui.chatting.ChattingUIActivity",
        "com.tencent.mm.ui.chatting.a",
        "com.tencent.mm.ui.conversation.f"
    )

    fun hook(classLoader: ClassLoader) {
        try {
            // 尝试 Hook ActionBar 或 Toolbar
            hookActionBar(classLoader)
            android.util.Log.i(TAG, "TopBar hooking initialized")
        } catch (e: Throwable) {
            android.util.Log.e(TAG, "TopBarHooker initialization failed", e)
        }
    }

    private fun hookActionBar(classLoader: ClassLoader) {
        try {
            // Hook ActionBar 的背景设置
            val actionBarClass = XposedHelpers.findClass(
                "android.app.ActionBar",
                classLoader
            )
            
            XposedHelpers.findAndHookMethod(
                actionBarClass,
                "setBackgroundDrawable",
                android.graphics.drawable.Drawable::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        try {
                            // 替换为半透明背景
                            param.args[0] = ColorDrawable(Color.argb(128, 0, 0, 0))
                        } catch (e: Exception) {
                            android.util.Log.e(TAG, "Error setting ActionBar background", e)
                        }
                    }
                }
            )
        } catch (e: Exception) {
            android.util.Log.w(TAG, "ActionBar hooking not available: ${e.message}")
        }
    }
}
