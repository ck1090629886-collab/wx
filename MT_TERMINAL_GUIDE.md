# 🚀 使用 MT 管理器终端编译 - 最简单的方法

## ✅ 为什么用 MT 管理器更好？

- ✅ 不需要安装 Termux
- ✅ 有 root 权限，直接访问项目
- ✅ 界面更友好
- ✅ 可以边看文件边编译

---

## 📱 完整步骤（MT 管理器）

### 第 1 步：打开 MT 管理器终端

1. 打开 **MT 管理器**
2. 点击左下角 **三横线菜单** ≡
3. 选择 **终端模拟器**
4. 会打开一个黑色命令行窗口

---

### 第 2 步：切换到 Root 权限

在终端中输入：

```bash
su
```

提示要授权，选择 **允许** / **授予**

看到提示符从 `$` 变成 `#` 就说明获得 root 了

---

### 第 3 步：进入项目目录

```bash
cd /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat
```

验证是否进入成功：
```bash
ls -la
```

应该看到 `build.gradle`、`app/` 等文件

---

### 第 4 步：安装 Java（只需一次）

#### 方法 A：使用 Termux 的 Java（推荐）

如果你已经安装了 Termux：

```bash
# 设置 Java 环境变量
export JAVA_HOME=/data/data/com.termux/files/usr/opt/openjdk
export PATH=$JAVA_HOME/bin:$PATH

# 验证
java -version
```

**如果 Java 可用，会显示版本号，直接跳到第 5 步**

#### 方法 B：安装 Termux 并配置 Java

**在 MT 终端中执行**：

```bash
# 先安装 Termux（从 MT 管理器下载区或 F-Droid）
# 然后在 Termux 中执行：
pkg install openjdk-17 -y

# 回到 MT 终端，设置环境变量
export JAVA_HOME=/data/data/com.termux/files/usr/opt/openjdk
export PATH=$JAVA_HOME/bin:$PATH
java -version
```

---

### 第 5 步：准备编译

```bash
# 给 gradlew 执行权限
chmod +x gradlew

# 检查权限
ls -la gradlew
```

应该看到 `-rwxr-xr-x` 开头（有 x 权限）

---

### 第 6 步：开始编译 ⏳

```bash
./gradlew assembleRelease --stacktrace
```

**这一步会花 20-40 分钟**

你会看到：
```
> Configuring project :app
> Task :app:preBuild
> Task :app:preReleaseBuild
...很多输出...
BUILD SUCCESSFUL in 25m 32s
26 actionable tasks: 26 executed
```

**看到 `BUILD SUCCESSFUL` 就成功了！**

---

### 第 7 步：找到并复制 APK

编译成功后：

```bash
# 复制 APK 到下载目录
cp app/build/outputs/apk/release/app-release.apk /sdcard/Download/BeautifulWeChat.apk

# 验证
ls -lh /sdcard/Download/BeautifulWeChat.apk
```

---

### 第 8 步：安装 APK

在 MT 管理器中：
1. 切换到 **文件** 模式
2. 进入 `/sdcard/Download/`
3. 找到 `BeautifulWeChat.apk`
4. 点击 → **查看（安装）**
5. 允许安装

---

### 第 9 步：在 LSPosed 中激活

1. 打开 **LSPosed 管理器**
2. **模块** 标签
3. 找到 **Beautiful WeChat** → 打开开关
4. 点击进入详情
5. **作用域** → 勾选 **微信**
6. 保存

---

### 第 10 步：重启微信

- 微信 → 长按 → 应用信息 → 强制停止
- 重新打开微信
- 进入聊天查看效果

---

## 🎯 完整命令（MT 终端中复制）

把这些命令一条条复制到 MT 终端：

```bash
# 1. 获取 root
su

# 2. 进入项目
cd /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat

# 3. 设置 Java（如果已安装 Termux）
export JAVA_HOME=/data/data/com.termux/files/usr/opt/openjdk
export PATH=$JAVA_HOME/bin:$PATH
java -version

# 4. 准备编译
chmod +x gradlew

# 5. 开始编译（20-40 分钟）
./gradlew assembleRelease --stacktrace

# 6. 复制 APK
cp app/build/outputs/apk/release/app-release.apk /sdcard/Download/BeautifulWeChat.apk
```

---

## 🆘 常见问题

### 问题 1：找不到 java 命令

**症状**：
```
bash: java: command not found
```

**解决方案 A**：安装 Termux 和 Java
```bash
# 从 F-Droid 或 MT 管理器下载区安装 Termux
# 打开 Termux 执行：
pkg install openjdk-17 -y

# 回到 MT 终端
export JAVA_HOME=/data/data/com.termux/files/usr/opt/openjdk
export PATH=$JAVA_HOME/bin:$PATH
java -version
```

**解决方案 B**：切换到 Termux
- 关闭 MT 终端
- 打开 Termux 应用
- 按照 `TERMUX_QUICK.md` 的步骤操作

---

### 问题 2：Permission denied

**症状**：
```
bash: ./gradlew: Permission denied
```

**解决**：
```bash
# 确保有 root
su

# 重新设置权限
chmod +x gradlew
chmod +x gradle/wrapper/gradle-wrapper.jar
```

---

### 问题 3：编译失败

**症状**：
```
FAILURE: Build failed with an exception.
```

**解决**：
```bash
# 查看详细错误
./gradlew assembleRelease --stacktrace --info

# 或清理后重试
./gradlew clean
./gradlew assembleRelease
```

---

### 问题 4：MT 终端卡住

**解决**：
- 按 **Ctrl + C**（MT 终端的虚拟按键）
- 或关闭终端重新打开
- 重新执行命令

---

## 💡 MT 管理器的优势

### 同时操作文件和终端

**分屏使用**：
1. 左边：文件浏览器，查看项目结构
2. 右边：终端，执行命令

**快速操作**：
- 编译时可以查看 `build.gradle`
- 编译完成立即看到 APK
- 直接安装 APK

---

## ⚡ 快速版（纯命令）

如果 Java 已经配置好：

```bash
su
cd /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat
chmod +x gradlew
./gradlew assembleRelease
cp app/build/outputs/apk/release/app-release.apk /sdcard/Download/
```

---

## 🎯 编译成功的标志

看到以下输出说明成功：

```
BUILD SUCCESSFUL in 25m 32s
26 actionable tasks: 26 executed
```

然后：
```bash
ls -lh /sdcard/Download/BeautifulWeChat.apk
```

显示文件存在，大小约 100-500 KB

---

## 📊 时间规划

| 步骤 | 时间 |
|------|------|
| 打开 MT 终端 | 即时 |
| 获取 root | 即时 |
| 配置 Java | 5 分钟（首次） |
| 编译项目 | 20-40 分钟 ⏳ |
| 复制安装 | 2 分钟 |
| **总计** | **30-50 分钟** |

---

## ✅ 完成后

APK 位置：`/sdcard/Download/BeautifulWeChat.apk`

安装并在 LSPosed 中激活后，微信聊天气泡会变成：
- 🟣 发送：蓝紫渐变
- 🟢 接收：黄绿渐变
- 圆角 + 白色文字

---

## 🔥 立即开始

1. **打开 MT 管理器**
2. **左下角 ≡ → 终端模拟器**
3. **复制上面的命令，一条条执行**
4. **等待编译完成**
5. **安装并享受美化效果**

---

**遇到问题？告诉我：**
- 看到的错误信息
- 卡在哪一步
- MT 终端显示了什么

我会立即帮你解决！🚀
