# 🚀 Termux 编译完整步骤

## 前置条件
- ✅ 已安装 Termux（从 F-Droid 或 GitHub）
- ✅ 项目在 `/data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat`
- ✅ 至少 5GB 可用存储空间
- ✅ 至少 4GB 可用内存（关闭其他大应用）

---

## 📱 在 Termux 中执行的命令

### 👉 直接复制粘贴以下命令到 Termux 中

```bash
# 第 1 步：更新软件包（约 2 分钟）
pkg update && pkg upgrade -y

# 第 2 步：安装 Java JDK 17（约 3 分钟）
pkg install openjdk-17 -y

# 验证 Java 安装（应该显示 java version）
java -version

# 第 3 步：安装其他工具（约 1 分钟）
pkg install wget unzip git -y

# 第 4 步：进入项目目录
cd /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat

# 第 5 步：给 gradlew 执行权限
chmod +x gradlew

# 第 6 步：开始编译（约 20-40 分钟，这是最长的步骤）
./gradlew assembleRelease --stacktrace

# 第 7 步：复制 APK 到可访问位置
cp app/build/outputs/apk/release/app-release.apk /sdcard/Download/

# 验证 APK 是否生成
ls -lh /sdcard/Download/app-release.apk
```

---

## 📊 时间分解

| 步骤 | 内容 | 时间 |
|------|------|------|
| 1 | 更新软件包 | 2 分钟 |
| 2 | 安装 Java | 3 分钟 |
| 3 | 安装工具 | 1 分钟 |
| 4 | 进入目录 | 即时 |
| 5 | 权限设置 | 即时 |
| 6 | **编译项目** | **20-40 分钟** ⏳ |
| 7 | 复制 APK | 即时 |
| **总计** | | **约 30-50 分钟** |

---

## 🎯 完整流程（一行一行）

### 第 1 步：打开 Termux
- 点击手机应用菜单，打开 Termux
- 黑色终端窗口打开

### 第 2 步：复制粘贴命令

在 Termux 中，长按屏幕会出现菜单，选择"粘贴"

依次执行：

```bash
# 第一块：准备环境
pkg update && pkg upgrade -y
pkg install openjdk-17 -y
java -version
```

**等待这块完成后**，继续：

```bash
# 第二块：安装工具
pkg install wget unzip git -y
```

**然后**：

```bash
# 第三块：进入项目并编译
cd /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat
chmod +x gradlew
./gradlew assembleRelease --stacktrace
```

⏳ **这一步会花 20-40 分钟，期间会看到大量输出。不要中断！**

### 第 3 步：获取 APK

编译完成后（看到绿色的 BUILD SUCCESSFUL），执行：

```bash
cp app/build/outputs/apk/release/app-release.apk /sdcard/Download/
ls -lh /sdcard/Download/app-release.apk
```

如果看到类似这样的输出，说明成功了：
```
-rw-r--r-- 1 root root 450K Nov  3 13:45 /sdcard/Download/app-release.apk
```

---

## 🎯 一键脚本（可选，更简单）

我已经为你创建了自动化脚本 `termux_build.sh`

在 Termux 中执行：

```bash
cd /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat
bash termux_build.sh
```

这个脚本会自动执行上面所有步骤。

---

## ⚠️ 可能出现的问题和解决方案

### 问题 1：`pkg update` 超慢或失败

**原因**：默认源在国外，网络慢

**解决**：编辑源文件
```bash
nano $PREFIX/etc/apt/sources.list
```

替换为清华源：
```
deb https://mirrors.tuna.tsinghua.edu.cn/termux/apt/termux-main stable main
```

保存（Ctrl+X, Y, Enter）后重新 `pkg update`

### 问题 2：`java -version` 未找到

**原因**：Java 安装失败或环境变量未设置

**解决**：
```bash
# 重新安装
pkg install openjdk-17 -y

# 确保 java 在 PATH 中
which java

# 如果还是不行，手动设置
export JAVA_HOME=$PREFIX/opt/openjdk
export PATH=$JAVA_HOME/bin:$PATH
java -version
```

### 问题 3：编译时 "Gradle daemon" 错误

**原因**：网络问题导致依赖下载失败

**解决**：重新运行编译
```bash
./gradlew assembleRelease --stacktrace
```

第二次通常会成功（因为已经下载了缓存）

### 问题 4：内存不足 (Out of Memory)

**症状**：编译中途突然停止，显示 "java.lang.OutOfMemoryError"

**解决**：
```bash
# 限制 Gradle 内存使用
export GRADLE_OPTS="-Xmx1024m"
./gradlew assembleRelease
```

### 问题 5：存储空间不足

**症状**：编译到一半显示 "No space left on device"

**解决**：
```bash
# 检查存储空间
df -h /sdcard

# 清理缓存
pkg clean

# 删除不需要的文件释放空间
```

### 问题 6：gradlew 权限错误

**症状**：`Permission denied` 或 `command not found: gradlew`

**解决**：
```bash
# 再次设置权限
chmod +x gradlew

# 或使用 bash 运行
bash gradlew assembleRelease
```

---

## ✅ 编译成功的标志

看到以下输出说明编译成功：

```
BUILD SUCCESSFUL in XXs
26 actionable tasks: 26 executed
```

以及：
```
✓ APK 已复制到：/sdcard/Download/app-release.apk
```

---

## 📲 获得 APK 后的操作

### 1. 检查 APK 是否存在

```bash
ls -lh /sdcard/Download/app-release.apk
```

如果显示文件存在，大小应该在 100-500 KB。

### 2. 使用文件管理器打开

- 打开文件管理器（如 MT 管理器）
- 进入 `/sdcard/Download/`
- 找到 `app-release.apk`
- 点击安装

### 3. 在 LSPosed 中激活

1. 打开 **LSPosed 管理器**
2. 进入 **模块** 标签页
3. 找到 **Beautiful WeChat**
4. 打开开关（变蓝）
5. 点击模块进入详情
6. 在 **作用域** 中勾选 **微信 (com.tencent.mm)**
7. 保存

### 4. 重启微信

- 进入微信，强制停止（设置 → 应用 → 微信 → 强制停止）
- 重新打开微信
- 进入任意聊天查看效果

---

## 🆘 完整命令快速查询

```bash
# 快速重新开始
cd /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat
./gradlew clean assembleRelease --stacktrace

# 只编译 debug 版本（更快，约 10 分钟）
./gradlew assembleDebug

# 查看编译进度
./gradlew assembleRelease --info

# 清理编译缓存
./gradlew clean

# 查看所有可用任务
./gradlew tasks
```

---

## 💡 优化建议

### 如果编译很慢

1. **关闭其他应用**释放内存
2. **使用国内镜像源**加快依赖下载
3. **开启 Gradle 守护进程**（已默认）

### 如果 Termux 卡住

1. 按 **Ctrl+C** 停止当前命令
2. 重新执行编译
3. 或者 `./gradlew assembleRelease --stop` 停止后台进程

---

## 📝 重要提醒

✅ **开始前**：
- 确保网络稳定
- 准备 5GB 存储空间
- 保持屏幕不锁定（可用 Termux 侧边栏锁定屏幕）
- 手机充电或接近满电

⏳ **编译中**：
- 不要关闭 Termux
- 不要重启手机
- 耐心等待（可以看看手机，它会自己运行）

✅ **编译成功后**：
- APK 会在 `/sdcard/Download/` 中
- 直接安装使用

---

## 🎯 下一步

1. **现在打开 Termux**
2. **复制粘贴上面的命令**
3. **开始编译**
4. **等待 30-50 分钟**
5. **获得 APK 并安装**
6. **在 LSPosed 中启用**
7. **重启微信查看效果**

---

**需要帮助？告诉我你遇到的问题，我会立即帮你解决！**

Current Status: 准备好开始了吗？
