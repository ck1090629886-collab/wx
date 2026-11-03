package com.beautifulwechat

import android.graphics.Color
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_MethodHook
import java.lang.reflect.Method

object BubbleHooker {
    
    private const val TAG = "BeautifulWeChat-Bubble"
    
    // 微信气泡相关类名（可能需要根据版本调整）
    private val BUBBLE_CLASS_NAMES = arrayOf(
        "com.tencent.mm.ui.chatting.ChattingItemBubble",
        "com.tencent.mm.ui.base.c",
        "com.tencent.mm.ui.chatting.d",
        "com.tencent.mm.ui.chatting.bubble.a",
        "com.tencent.mm.ui.chatting.k"
    )
    
    // 颜色配置
    private const val SEND_GRADIENT_START = 0xFF5B4FFF.toInt()  // 蓝紫
    private const val SEND_GRADIENT_END = 0xFFA855F7.toInt()    // 紫罗兰
    private const val RECEIVE_GRADIENT_START = 0xFFFDE047.toInt() // 黄色
    private const val RECEIVE_GRADIENT_END = 0xFF84CC16.toInt()   // 绿色
    private const val TEXT_COLOR = Color.WHITE
    private const val CORNER_RADIUS = 14f
    
    // 缓存的 Drawable 对象，避免重复创建
    private val drawableCache = mutableMapOf<String, Any>()

    fun hook(classLoader: ClassLoader) {
        try {
            // 尝试找到并 Hook 气泡类
            for (className in BUBBLE_CLASS_NAMES) {
                try {
                    val bubbleClass = XposedHelpers.findClass(className, classLoader)
                    hookBubbleClass(bubbleClass)
                    Log.i(TAG, "Successfully hooked: $className")
                    return
                } catch (e: Exception) {
                    // 继续尝试下一个类名
                }
            }
            
            Log.w(TAG, "Could not find bubble class, trying dynamic search...")
            hookByDynamicSearch(classLoader)
            
        } catch (e: Throwable) {
            Log.e(TAG, "BubbleHooker initialization failed", e)
        }
    }

    private fun hookBubbleClass(bubbleClass: Class<*>) {
        // Hook setBackground 方法
        XposedHelpers.findAndHookMethod(
            bubbleClass,
            "setBackground",
            Object::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    try {
                        val view = param.thisObject
                        if (view is FrameLayout) {
                            val isSender = isSenderBubble(view)
                            val drawable = createBubbleDrawable(isSender)
                            view.background = drawable
                            // 强制文字颜色为白色
                            setTextColorRecursive(view, TEXT_COLOR)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error in setBackground hook", e)
                    }
                }
            }
        )
        
        // Hook onDraw 方法
        try {
            XposedHelpers.findAndHookMethod(
                bubbleClass,
                "onDraw",
                android.graphics.Canvas::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        try {
                            val view = param.thisObject
                            if (view is FrameLayout && view.background == null) {
                                val isSender = isSenderBubble(view)
                                val drawable = createBubbleDrawable(isSender)
                                view.background = drawable
                                setTextColorRecursive(view, TEXT_COLOR)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error in onDraw hook", e)
                        }
                    }
                }
            )
        } catch (e: Exception) {
            // onDraw 可能不存在，不影响主要功能
        }
    }

    private fun hookByDynamicSearch(classLoader: ClassLoader) {
        // 尝试通过查找视图来动态定位气泡类
        try {
            // 这是一个 fallback 策略，实际使用中需要根据微信版本调整
            Log.i(TAG, "Dynamic search for bubble classes - fallback mode")
        } catch (e: Exception) {
            Log.e(TAG, "Dynamic search failed", e)
        }
    }

    private fun createBubbleDrawable(isSender: Boolean): android.graphics.drawable.Drawable {
        val (startColor, endColor) = if (isSender) {
            Pair(SEND_GRADIENT_START, SEND_GRADIENT_END)
        } else {
            Pair(RECEIVE_GRADIENT_START, RECEIVE_GRADIENT_END)
        }
        
        return GradientBubbleDrawable(
            startColor = startColor,
            endColor = endColor,
            cornerRadius = CORNER_RADIUS,
            alpha = 217, // 85% 不透明
            isSender = isSender
        )
    }

    private fun isSenderBubble(view: Any): Boolean {
        // 检查视图的位置或标签来判断是否为发送气泡
        return try {
            val tag = XposedHelpers.getObjectField(view, "tag")
            tag?.toString()?.contains("send", ignoreCase = true) ?: false
        } catch (e: Exception) {
            false
        }
    }

    private fun setTextColorRecursive(view: android.view.View, color: Int) {
        if (view is TextView) {
            view.setTextColor(color)
        }
        if (view is android.view.ViewGroup) {
            for (i in 0 until view.childCount) {
                setTextColorRecursive(view.getChildAt(i), color)
            }
        }
    }
}
