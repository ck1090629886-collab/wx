# Beautiful WeChat LSPosed Module

## 📁 项目完成清单

### ✅ 已完成
- [x] Gradle 项目结构配置
- [x] AndroidManifest.xml 配置
- [x] Xposed 模块初始化（xposed_init）
- [x] LSPosed 模块入口（MainHook.kt）
- [x] 气泡美化逻辑（BubbleHooker.kt）
- [x] 顶栏美化逻辑（TopBarHooker.kt）
- [x] 渐变效果实现（GradientBubbleDrawable.kt）
- [x] 资源文件和颜色配置
- [x] ProGuard 混淆规则
- [x] 详细的 README 文档
- [x] 快速使用指南
- [x] 编译脚本（build.sh）
- [x] 错误日志处理

### 📊 项目统计

**代码文件**：
- MainHook.kt - 主入口（~100 行）
- BubbleHooker.kt - 气泡美化（~150 行）
- TopBarHooker.kt - 顶栏美化（~80 行）
- GradientBubbleDrawable.kt - 渐变实现（~120 行）

**配置文件**：
- AndroidManifest.xml
- build.gradle（项目级）
- app/build.gradle（应用级）
- settings.gradle
- proguard-rules.pro
- strings.xml

**文档**：
- README.md（完整说明）
- QUICKSTART.md（快速指南）
- IMPLEMENTATION.md（本文件）

### 🎯 核心功能

#### 气泡美化
- 蓝紫渐变发送气泡（#5B4FFF → #A855F7）
- 黄绿互补接收气泡（#FDE047 → #84CC16）
- 对角线性渐变
- 14dp 圆角
- 85% 不透明度 + 轻度模糊
- 轻微投影效果

#### 顶栏效果
- 半透明毛玻璃效果
- 透视背景

#### 防护措施
- 零数据修改（纯 UI Hook）
- 详细错误日志
- 异常捕获和降级
- 性能优化（缓存、硬件加速）

### 🔧 如何编译

#### 环境要求
- Java JDK 11+
- Android SDK（API 33）
- Gradle 8.0.2+

#### 编译步骤
```bash
# 进入项目目录
cd BeautifulWeChat

# 方法 1：使用编译脚本
./build.sh

# 方法 2：手动编译
gradle assembleRelease

# 输出
# app/build/outputs/apk/release/app-release.apk
```

### 📦 目录结构

```
BeautifulWeChat/
├── app/
│   ├── src/main/
│   │   ├── java/com/beautifulwechat/
│   │   │   ├── MainHook.kt
│   │   │   ├── BubbleHooker.kt
│   │   │   ├── TopBarHooker.kt
│   │   │   └── GradientBubbleDrawable.kt
│   │   ├── res/values/
│   │   │   └── strings.xml
│   │   ├── assets/
│   │   │   └── xposed_init
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
├── build.sh
├── README.md
├── QUICKSTART.md
└── IMPLEMENTATION.md (本文件)
```

### 🚀 使用流程

1. **安装**
   ```bash
   adb install app-release.apk
   ```

2. **启用**（在 LSPosed 中）
   - 模块列表 → 找到 Beautiful WeChat → 启用
   - 点击进入详情 → 作用域 → 勾选微信
   - 保存

3. **重启微信**
   - 强制停止
   - 重新打开

4. **验证**
   - 发送和接收消息
   - 观察气泡颜色是否变化

### ⚙️ 自定义配置

#### 修改颜色
编辑 `BubbleHooker.kt` 中的常量：
```kotlin
private const val SEND_GRADIENT_START = 0xFF5B4FFF.toInt()
private const val SEND_GRADIENT_END = 0xFFA855F7.toInt()
private const val RECEIVE_GRADIENT_START = 0xFFFDE047.toInt()
private const val RECEIVE_GRADIENT_END = 0xFF84CC16.toInt()
```

#### 修改圆角
```kotlin
private const val CORNER_RADIUS = 14f  // 单位：dp
```

#### 修改透明度
编辑 `GradientBubbleDrawable.kt`：
```kotlin
private val alpha: Int = 217  // 0-255，217 = 85%
```

### 🔍 调试

**查看日志**：
```bash
# 模块日志
cat /sdcard/Android/data/com.beautifulwechat/error.log

# LSPosed 框架日志
# 在 LSPosed 管理器中查看
```

**常见问题调试**：
- 如果气泡不变色：检查 Hook 的类名是否正确
- 如果应用闪退：查看日志中的异常堆栈
- 如果文字颜色不对：检查 `setTextColorRecursive` 逻辑

### 📄 版本信息

- **模块版本**：1.0.0
- **API 级别**：24（Android 7.0）
- **目标 API**：33（Android 13）
- **Xposed API**：82+

### ⚖️ 许可证

MIT License - 可自由使用、修改和分发

### 🎓 学习资源

本项目可作为学习以下内容的参考：
- Xposed/LSPosed 框架原理
- Android Kotlin 开发
- 动态 Hook 技术
- 自定义 Drawable 实现
- 反射调用 API

### 🙏 致谢

感谢以下项目和社区：
- LSPosed 团队
- Android Xposed 社区
- 所有贡献者

---

**项目状态**：✅ 完成 | **最后更新**：2026-11-03
