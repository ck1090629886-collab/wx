package com.beautifulwechat

import android.util.Log
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import com.beautifulwechat.hook.BubbleHookStrategy
import com.beautifulwechat.hook.TopBarHookStrategy
import com.beautifulwechat.util.Logger
import java.util.concurrent.atomic.AtomicBoolean

class ModuleEntry : XposedModule() {
    
    private val installed = AtomicBoolean(false)
    @Volatile
    private var loadedProcess: String? = null

    override fun onModuleLoaded(param: XposedModuleInterface.ModuleLoadedParam) {
        loadedProcess = param.processName
        Logger.info(
            "event=module_loaded process=${param.processName} " +
            "api=${getApiVersion()} framework=${getFrameworkName()}"
        )
    }

    override fun onPackageReady(param: XposedModuleInterface.PackageReadyParam) {
        // 只在微信主进程中安装
        if (param.packageName != TARGET_PACKAGE) return
        if (loadedProcess?.startsWith(TARGET_PACKAGE) != true) return
        
        installHooks(param.classLoader)
    }

    private fun installHooks(classLoader: ClassLoader) {
        if (!installed.compareAndSet(false, true)) {
            Logger.info("event=install_skipped reason=already_installed")
            return
        }

        try {
            Logger.info("event=install_start package=$TARGET_PACKAGE")
            
            // 安装气泡美化 Hook
            val bubbleHooker = BubbleHookStrategy(this, classLoader)
            bubbleHooker.install()
            
            // 安装顶栏美化 Hook
            val topBarHooker = TopBarHookStrategy(this, classLoader)
            topBarHooker.install()
            
            Logger.info("event=install_success hooks_count=2")
            
        } catch (t: Throwable) {
            installed.set(false)
            Logger.error("event=install_failed", t)
        }
    }

    companion object {
        private const val TARGET_PACKAGE = "com.tencent.mm"
    }
}
