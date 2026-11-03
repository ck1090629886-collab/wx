# Beautiful WeChat - 微信聊天界面美化模块

一个基于 LSPosed 的微信聊天界面美化模块，提供渐变色气泡和毛玻璃效果，不修改任何数据或协议，确保安全不封号。

## ✨ 功能特性

### 🎨 聊天气泡美化
- **发送消息**：蓝紫对角渐变（#5B4FFF → #A855F7）
- **接收消息**：黄绿互补对角渐变（#FDE047 → #84CC16）
- **圆角设计**：14dp 圆角半径，柔和舒适
- **毛玻璃效果**：85% 不透明度 + 轻度模糊（4-8dp）
- **轻微阴影**：增加立体感
- **白色文字**：与渐变背景形成高对比度

### 🪟 顶栏毛玻璃
- 半透明顶栏，透视聊天背景
- 自然层次感，现代化设计

## 📋 技术特点

### 🛡️ 安全设计
- **零数据修改**：只 Hook 视图层绘制方法
- **不触碰协议层**：不拦截消息、加密或存储
- **本地运行**：无网络请求，无外部依赖
- **防封号设计**：理论上微信无法检测纯 UI 修改

### ⚡ 性能优化
- Drawable 对象缓存，避免重复创建
- 硬件加速渲染
- 最小化 Hook 点，降低性能开销

## 📦 安装方法

### 前置要求
1. 已 Root 的 Android 设备（Android 7.0+）
2. 已安装 LSPosed 框架
3. 微信 8.0.80（推荐版本）

### 安装步骤

#### 方法一：使用预编译 APK
1. 下载 `BeautifulWeChat-v1.0.0.apk`
2. 安装到手机
3. 在 LSPosed 管理器中：
   - 进入"模块"页面
   - 启用 "Beautiful WeChat"
   - 勾选作用域：微信（com.tencent.mm）
4. 重启微信

#### 方法二：从源码编译
1. 安装 Android Studio 或 Android SDK
2. 克隆/下载本项目
3. 在项目根目录执行：
   ```bash
   ./gradlew assembleRelease
   ```
4. 编译完成后，APK 位于：
   `app/build/outputs/apk/release/app-release.apk`
5. 按照方法一的步骤 2-4 安装

## 🎯 支持的微信版本

- **主要适配**：微信 8.0.80
- **理论兼容**：微信 8.0.x 系列（可能需要调整类名）

## 📝 目录结构

```
BeautifulWeChat/
├── app/
│   ├── src/main/
│   │   ├── java/com/beautifulwechat/
│   │   │   ├── MainHook.kt                # LSPosed 入口
│   │   │   ├── BubbleHooker.kt            # 气泡美化逻辑
│   │   │   ├── TopBarHooker.kt            # 顶栏美化逻辑
│   │   │   └── GradientBubbleDrawable.kt  # 渐变Drawable
│   │   ├── res/values/
│   │   │   └── strings.xml                # 资源配置
│   │   ├── assets/
│   │   │   └── xposed_init                # Xposed 入口声明
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
└── README.md
```

## 🔧 故障排查

### 模块未生效
1. **检查 LSPosed 状态**
   - 确认 LSPosed 框架正常运行
   - 检查模块是否已启用
   - 确认微信已被勾选为作用域

2. **查看日志**
   - 日志路径：`/sdcard/Android/data/com.beautifulwechat/error.log`
   - 查看 LSPosed 日志：LSPosed 管理器 → 日志

3. **版本不兼容**
   - 如果你的微信版本不是 8.0.80，可能需要调整 Hook 的类名
   - 查看日志中的 "Could not find bubble class" 提示

### 只有部分气泡生效
- 微信的气泡类名可能在不同版本中变化
- 需要通过反编译微信找到正确的类名并更新 `BubbleHooker.kt` 中的 `BUBBLE_CLASS_NAMES`

### 文字颜色不对
- 某些特殊消息类型（如系统通知）可能不受影响
- 这是正常的，模块只美化普通聊天气泡

## 🛠️ 自定义配置

如果你想修改颜色或效果，可以编辑以下文件：

### 修改气泡颜色
编辑 `BubbleHooker.kt`：
```kotlin
// 发送气泡颜色
private const val SEND_GRADIENT_START = 0xFF5B4FFF.toInt()  // 蓝紫
private const val SEND_GRADIENT_END = 0xFFA855F7.toInt()    // 紫罗兰

// 接收气泡颜色
private const val RECEIVE_GRADIENT_START = 0xFFFDE047.toInt() // 黄色
private const val RECEIVE_GRADIENT_END = 0xFF84CC16.toInt()   // 绿色
```

### 修改圆角大小
编辑 `BubbleHooker.kt`：
```kotlin
private const val CORNER_RADIUS = 14f  // 改成你想要的值（单位：dp）
```

### 修改透明度
编辑 `GradientBubbleDrawable.kt`：
```kotlin
private val alpha: Int = 217  // 85% 不透明度，改成 0-255 之间的值
```

## ⚠️ 免责声明

1. 本模块仅用于学习和研究目的
2. 使用本模块可能违反微信服务条款
3. 虽然设计上安全，但任何第三方修改都存在潜在风险
4. 使用者需自行承担所有风险，包括但不限于账号封禁
5. 作者不对使用本模块造成的任何后果负责

## 📄 许可证

MIT License

## 🙏 致谢

- [LSPosed](https://github.com/LSPosed/LSPosed) - 强大的 Xposed 框架
- Android Xposed API

## 📧 联系方式

如有问题或建议，请：
- 提交 Issue
- 发起 Pull Request

---

**注意**：本模块完全开源，不含任何恶意代码或数据收集功能。所有代码均可在此仓库中查阅。
