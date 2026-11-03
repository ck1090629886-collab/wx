package com.beautifulwechat.hook

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.beautifulwechat.drawable.GradientBubbleDrawable
import com.beautifulwechat.util.Logger
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedInterface.BeforeHookCallback
import io.github.libxposed.api.XposedInterface.AfterHookCallback

class BubbleHookStrategy(
    private val module: XposedModule,
    private val classLoader: ClassLoader
) {
    
    companion object {
        private val BUBBLE_CLASS_CANDIDATES = arrayOf(
            "com.tencent.mm.ui.chatting.view.MMChatItemBubble",
            "com.tencent.mm.ui.chatting.ChattingItemBubble",
            "com.tencent.mm.ui.chatting.viewitems.BaseChatItemLayout",
            "com.tencent.mm.ui.base.MMFakeBoldTextView"
        )
        
        private const val SEND_GRADIENT_START = 0xFF5B4FFF.toInt()
        private const val SEND_GRADIENT_END = 0xFFA855F7.toInt()
        private const val RECEIVE_GRADIENT_START = 0xFFFDE047.toInt()
        private const val RECEIVE_GRADIENT_END = 0xFF84CC16.toInt()
        private const val TEXT_COLOR = Color.WHITE
        private const val CORNER_RADIUS = 14f
        
        private val drawableCache = mutableMapOf<Boolean, GradientBubbleDrawable>()
    }
    
    fun install() {
        var hookedClass: Class<*>? = null
        
        for (className in BUBBLE_CLASS_CANDIDATES) {
            try {
                val clazz = classLoader.loadClass(className)
                hookedClass = clazz
                Logger.info("event=bubble_class_found class=$className")
                break
            } catch (e: ClassNotFoundException) {
                // 继续
            }
        }
        
        if (hookedClass == null) {
            Logger.warn("event=bubble_class_not_found candidates=${BUBBLE_CLASS_CANDIDATES.size}")
            return
        }
        
        hookSetBackground(hookedClass)
        hookOnAttachedToWindow(hookedClass)
    }
    
    private fun hookSetBackground(targetClass: Class<*>) {
        try {
            val method = targetClass.getDeclaredMethod(
                "setBackground",
                android.graphics.drawable.Drawable::class.java
            )
            
            module.hook(method, object : BeforeHookCallback {
                override fun beforeHookedMethod(callback: XposedInterface.MethodHookCallback) {
                    try {
                        val view = callback.thisObject as? View
                        if (view != null && shouldApplyBubbleStyle(view)) {
                            val isSender = isSenderBubble(view)
                            val drawable = getCachedDrawable(isSender)
                            callback.args[0] = drawable
                            setTextColorRecursive(view, TEXT_COLOR)
                        }
                    } catch (e: Exception) {
                        Logger.error("event=hook_execution_failed method=setBackground", e)
                    }
                }
            })
            
            Logger.info("event=hook_registered method=setBackground")
            
        } catch (e: NoSuchMethodException) {
            Logger.warn("event=method_not_found method=setBackground")
        } catch (e: Exception) {
            Logger.error("event=hook_failed method=setBackground", e)
        }
    }
    
    private fun hookOnAttachedToWindow(targetClass: Class<*>) {
        try {
            val method = targetClass.getDeclaredMethod("onAttachedToWindow")
            
            module.hook(method, object : AfterHookCallback {
                override fun afterHookedMethod(callback: XposedInterface.MethodHookCallback) {
                    try {
                        val view = callback.thisObject as? View
                        if (view != null && shouldApplyBubbleStyle(view)) {
                            view.post {
                                applyBubbleStyle(view)
                            }
                        }
                    } catch (e: Exception) {
                        Logger.error("event=hook_execution_failed method=onAttachedToWindow", e)
                    }
                }
            })
            
            Logger.info("event=hook_registered method=onAttachedToWindow")
            
        } catch (e: NoSuchMethodException) {
            Logger.warn("event=method_not_found method=onAttachedToWindow")
        } catch (e: Exception) {
            Logger.error("event=hook_failed method=onAttachedToWindow", e)
        }
    }
    
    private fun shouldApplyBubbleStyle(view: View): Boolean {
        return try {
            val parent = view.parent
            parent != null && parent is ViewGroup
        } catch (e: Exception) {
            false
        }
    }
    
    private fun applyBubbleStyle(view: View) {
        try {
            val isSender = isSenderBubble(view)
            val drawable = getCachedDrawable(isSender)
            view.background = drawable
            setTextColorRecursive(view, TEXT_COLOR)
        } catch (e: Exception) {
            Logger.error("event=apply_style_failed", e)
        }
    }
    
    private fun getCachedDrawable(isSender: Boolean): GradientBubbleDrawable {
        return drawableCache.getOrPut(isSender) {
            val (startColor, endColor) = if (isSender) {
                Pair(SEND_GRADIENT_START, SEND_GRADIENT_END)
            } else {
                Pair(RECEIVE_GRADIENT_START, RECEIVE_GRADIENT_END)
            }
            
            GradientBubbleDrawable(
                startColor = startColor,
                endColor = endColor,
                cornerRadius = CORNER_RADIUS,
                alpha = 217,
                isSender = isSender
            )
        }
    }
    
    private fun isSenderBubble(view: View): Boolean {
        return try {
            val tag = view.tag
            if (tag != null && tag.toString().contains("send", ignoreCase = true)) {
                return true
            }
            
            val id = view.id
            if (id > 0) {
                try {
                    val resourceName = view.resources.getResourceEntryName(id)
                    if (resourceName.contains("send", ignoreCase = true) ||
                        resourceName.contains("right", ignoreCase = true)) {
                        return true
                    }
                } catch (e: Exception) {
                    // 忽略
                }
            }
            
            val parent = view.parent as? ViewGroup
            if (parent != null) {
                val x = view.left
                val parentWidth = parent.width
                return x > parentWidth / 2
            }
            
            false
        } catch (e: Exception) {
            false
        }
    }
    
    private fun setTextColorRecursive(view: View, color: Int) {
        try {
            if (view is TextView) {
                view.setTextColor(color)
            }
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    setTextColorRecursive(view.getChildAt(i), color)
                }
            }
        } catch (e: Exception) {
            // 忽略
        }
    }
}
