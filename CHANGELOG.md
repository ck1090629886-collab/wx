# Beautiful WeChat v2.0.0 更新日志

## [2.0.0] - 2024-11-03

### 🔄 重大变更
- **完全重构**：从旧 Xposed API 迁移到现代 libxposed API 102
- **架构升级**：采用分层架构，代码更清晰、更易维护
- **配置现代化**：使用标准 LSPosed 配置文件

### ✨ 新增功能
- 结构化日志系统（event=xxx key=value 格式）
- 文件持久化日志（/sdcard/Android/data/com.beautifulwechat/）
- 防御性错误处理和异常保护
- 智能进程和包名判断
- 多候选类名适配不同微信版本

### 🚀 性能优化
- Drawable 对象缓存，避免重复创建
- Gradient 智能重建，只在必要时更新
- 最小化 Hook 点，降低性能开销
- 优化内存使用

### 🛡️ 稳定性提升
- 使用 ExceptionMode.DEFAULT 跟随模块配置
- AtomicBoolean 防止重复安装
- 完整的异常捕获和日志记录
- Hook 失败自动回退

### 📦 技术栈更新
- libxposed API: 102.0.0
- Kotlin: 1.9.20
- Gradle: 8.2.0
- Android Gradle Plugin: 8.2.0
- Target SDK: 34
- Compile SDK: 34
- Java: 17

### 🔧 开发体验
- 完整的 ProGuard 规则
- 支持代码混淆
- 模块化代码结构
- 清晰的日志输出

### 📝 文档改进
- 全新 README.md
- 升级检查清单
- 详细的故障排查指南
- 自定义配置说明

### ⚠️ 破坏性变化
- 不兼容 v1.x 配置，需要重新安装
- LSPosed 需要重新勾选作用域
- 旧的 `assets/xposed_init` 已移除
- 入口类从 `MainHook` 改为 `ModuleEntry`

### 🐛 修复问题
- 修复 Drawable 重复创建导致的性能问题
- 修复缺少异常处理导致的崩溃
- 修复类加载器处理不当的问题
- 修复日志文件权限问题

### 📋 已知问题
- 微信不同版本可能需要调整类名
- 首次安装建议重启微信
- 某些特殊消息类型可能不受影响

---

## [1.0.0] - 之前版本

### 功能
- 基本的聊天气泡渐变效果
- 顶栏毛玻璃效果
- 使用旧 Xposed API

### 限制
- 使用过时的 API
- 缺少完整的错误处理
- 性能优化不足
- 代码结构混乱
