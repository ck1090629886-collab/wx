# LSPosed API 102 规则
-dontwarn io.github.libxposed.annotation.**
-adaptresourcefilecontents META-INF/xposed/java_init.list
-keep,allowoptimization,allowobfuscation public class * extends io.github.libxposed.api.XposedModule {
    public <init>();
}

# 保留模块入口
-keep class com.beautifulwechat.ModuleEntry {
    public <init>();
}

# 保留配置类
-keep class com.beautifulwechat.config.** { *; }

# 保留日志工具
-keep class com.beautifulwechat.util.Logger { *; }
