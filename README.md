# Beautiful WeChat - 微信聊天界面美化模块

基于 **LSPosed API 102** 的现代化微信聊天界面美化模块，提供渐变色气泡和毛玻璃效果。

## ✨ 版本 2.0.0 重大更新

### 🔄 技术架构升级
- ✅ **迁移到 libxposed API 102**：使用现代 Xposed API，更稳定、更安全
- ✅ **完整的模块元数据**：`module.prop`、`scope.list`、`java_init.list`
- ✅ **防御性错误处理**：异常保护、日志系统、回退机制
- ✅ **性能优化**：Drawable 缓存、最小化 Hook 点
- ✅ **代码分层**：入口、策略、工具分离，易于维护

### 🎨 功能特性
- **发送消息**：蓝紫对角渐变（#5B4FFF → #A855F7）
- **接收消息**：黄绿互补对角渐变（#FDE047 → #84CC16）
- **圆角设计**：14dp 圆角半径，柔和舒适
- **毛玻璃效果**：85% 不透明度 + 轻度模糊
- **轻微阴影**：增加立体感
- **白色文字**：高对比度，清晰易读
- **顶栏毛玻璃**：半透明顶栏，现代化设计

## 🛡️ 安全设计

### 零数据修改
- ✅ 只 Hook 视图层绘制方法
- ✅ 不触碰消息、加密或存储
- ✅ 不拦截网络请求
- ✅ 本地运行，无外部依赖
- ✅ 防封号设计（理论上微信无法检测纯 UI 修改）

### 性能优化
- ✅ Drawable 对象缓存
- ✅ 硬件加速渲染
- ✅ 最小化 Hook 点
- ✅ 延迟加载策略

## 📦 安装方法

### 前置要求
1. ✅ Root 设备（Android 7.0+）
2. ✅ LSPosed 框架（推荐最新版）
3. ✅ 微信 8.0.x 版本

### 安装步骤

#### 方法一：使用预编译 APK
1. 下载 `BeautifulWeChat-v2.0.0.apk`
2. 安装到手机
3. 在 LSPosed 管理器中：
   - 进入"模块"页面
   - 启用 "Beautiful WeChat"
   - 勾选作用域：**微信（com.tencent.mm）**
4. **重启微信**或强制停止后重新打开

#### 方法二：从源码编译

**使用 Termux（推荐）：**
```bash
# 进入项目目录
cd /storage/emulated/0/BeautifulWeChat

# 编译
./gradlew assembleRelease

# APK 位置
ls -lh app/build/outputs/apk/release/
```

**使用 Android Studio：**
1. 导入项目
2. Build → Build Bundle(s) / APK(s) → Build APK(s)
3. 在 `app/build/outputs/apk/release/` 找到 APK

## 📁 项目结构（v2.0）

```
BeautifulWeChat/
├── app/
│   ├── src/main/
│   │   ├── java/com/beautifulwechat/
│   │   │   ├── ModuleEntry.kt              # API 102 入口
│   │   │   ├── hook/
│   │   │   │   ├── BubbleHookStrategy.kt   # 气泡 Hook 策略
│   │   │   │   └── TopBarHookStrategy.kt   # 顶栏 Hook 策略
│   │   │   ├── drawable/
│   │   │   │   └── GradientBubbleDrawable.kt # 渐变 Drawable
│   │   │   └── util/
│   │   │       └── Logger.kt               # 结构化日志
│   │   ├── resources/META-INF/xposed/
│   │   │   ├── java_init.list              # 入口声明
│   │   │   ├── module.prop                 # 模块配置
│   │   │   └── scope.list                  # 作用域
│   │   ├── res/values/
│   │   │   └── strings.xml                 # 资源配置
│   │   └── AndroidManifest.xml
│   ├── build.gradle                        # 应用级构建
│   └── proguard-rules.pro                  # 混淆规则
├── build.gradle                            # 项目级构建
├── settings.gradle
└── README.md
```

## 🔧 故障排查

### 模块未生效

**1. 检查 LSPosed 状态**
```bash
# 查看日志
adb logcat | grep BeautifulWeChat
```

**2. 检查作用域**
- LSPosed 管理器 → Beautiful WeChat → 作用域
- 确保勾选了 **微信（com.tencent.mm）**
- 修改后需要重启微信

**3. 查看模块日志**
```bash
# 日志文件位置
/sdcard/Android/data/com.beautifulwechat/beautifulwechat.log
```

日志示例：
```
2024-11-03 19:30:00.123 [INFO] event=module_loaded process=com.tencent.mm api=102
2024-11-03 19:30:00.456 [INFO] event=bubble_class_found class=com.tencent.mm.ui.chatting.view.MMChatItemBubble
2024-11-03 19:30:00.789 [INFO] event=hook_registered method=setBackground
```

**4. 版本兼容性**
如果微信版本不兼容，日志会显示：
```
[WARN] event=bubble_class_not_found candidates=4
```

解决方法：
1. 使用 jadx 或其他工具反编译微信 APK
2. 查找包含 "Bubble" 或 "ChatItem" 的类
3. 修改 `BubbleHookStrategy.kt` 中的 `BUBBLE_CLASS_CANDIDATES`
4. 重新编译安装

### 只有部分气泡生效
- 微信的气泡类名在不同版本中可能变化
- 检查日志中的 `bubble_class_found` 事件
- 可能需要根据你的微信版本调整类名列表

## 🎨 自定义配置

### 修改气泡颜色
编辑 `BubbleHookStrategy.kt`：
```kotlin
// 发送气泡颜色
private const val SEND_GRADIENT_START = 0xFF5B4FFF.toInt()  // 改为你的颜色
private const val SEND_GRADIENT_END = 0xFFA855F7.toInt()

// 接收气泡颜色
private const val RECEIVE_GRADIENT_START = 0xFFFDE047.toInt()
private const val RECEIVE_GRADIENT_END = 0xFF84CC16.toInt()
```

### 修改圆角大小
编辑 `BubbleHookStrategy.kt`：
```kotlin
private const val CORNER_RADIUS = 14f  // 改为你想要的值（dp）
```

### 修改透明度
编辑 `GradientBubbleDrawable.kt` 或调用时修改：
```kotlin
alpha = 217  // 85% 不透明度，范围 0-255
```

## 🆚 v1.x 到 v2.0 的变化

| 项目 | v1.x | v2.0 |
|------|------|------|
| API | 旧 Xposed API | libxposed API 102 |
| 入口 | `IXposedHookLoadPackage` | `XposedModule` |
| 配置 | `assets/xposed_init` | `META-INF/xposed/*` |
| 依赖 | 无明确依赖 | `compileOnly api:102.0.0` |
| 错误处理 | 基础 try-catch | 防御性 Guard + 结构化日志 |
| 性能 | 重复创建 Drawable | 对象缓存 |
| 代码结构 | 单文件混合 | 分层架构 |
| 混淆支持 | 不完整 | 完整 ProGuard 规则 |

## ⚠️ 免责声明

1. 本模块仅用于**学习和研究**目的
2. 使用本模块可能违反微信服务条款
3. 虽然设计上安全，但任何第三方修改都存在潜在风险
4. 使用者需自行承担所有风险，包括但不限于账号封禁
5. 作者不对使用本模块造成的任何后果负责

## 📄 技术细节

### 为什么选择 API 102？
- ✅ 现代化 API 设计
- ✅ 更好的类型安全
- ✅ 内置异常保护模式
- ✅ 支持 Hot Reload（未来可扩展）
- ✅ 官方推荐和长期支持

### Hook 策略
1. **优先 Hook setBackground**：直接替换背景
2. **备用 Hook onAttachedToWindow**：视图附加时应用样式
3. **多候选类名**：适配不同微信版本
4. **智能判断发送方**：通过 tag、id、布局位置多重判断

### 日志系统
- ✅ 结构化日志（event=xxx key=value）
- ✅ 分级别（INFO/WARN/ERROR）
- ✅ 文件持久化
- ✅ Logcat 实时输出

## 📧 反馈与贡献

- 提交 Issue：报告 Bug 或建议
- Pull Request：贡献代码
- 日志分享：附带完整日志帮助定位问题

## 📄 许可证

MIT License

## 🙏 致谢

- [LSPosed](https://github.com/LSPosed/LSPosed) - 强大的 Xposed 框架
- [libxposed](https://github.com/libxposed/api) - 现代化 Xposed API

---

**v2.0.0** | 完全重构 | API 102 | 生产级代码质量
