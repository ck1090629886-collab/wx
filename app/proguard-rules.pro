# 保护我们的 Hook 类
-keep class com.beautifulwechat.** { *; }
-keep class com.beautifulwechat.MainHook { *; }
-keep class com.beautifulwechat.BubbleHooker { *; }
-keep class com.beautifulwechat.TopBarHooker { *; }
-keep class com.beautifulwechat.GradientBubbleDrawable { *; }

# 保护与 Xposed 相关的类
-keep class de.robv.android.xposed.** { *; }
-keep interface de.robv.android.xposed.** { *; }

# 保护 Android 框架类
-keep class android.** { *; }
-keep interface android.** { *; }

# 保护 Kotlin 相关
-keep class kotlin.** { *; }
-keep interface kotlin.** { *; }

# 通用规则
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
