# 📱 无电脑编译方案 - 使用 Termux

如果你没有电脑，可以在 Android 设备上使用 Termux 完成编译。

## 🚀 快速开始

### 1. 安装 Termux

从以下渠道之一安装 Termux：
- F-Droid: https://f-droid.org/en/packages/com.termux/
- GitHub Releases: https://github.com/termux/termux-app/releases

⚠️ **不要从 Google Play 安装**，版本已过时！

### 2. 在 Termux 中安装编译环境

```bash
# 更新软件包
pkg update && pkg upgrade -y

# 安装 Java 和必要工具
pkg install openjdk-17 -y
pkg install git wget -y

# 验证安装
java -version
```

### 3. 安装 Gradle

```bash
# 下载 Gradle
cd ~
wget https://services.gradle.org/distributions/gradle-8.0.2-bin.zip

# 解压
pkg install unzip -y
unzip gradle-8.0.2-bin.zip

# 配置环境变量
echo 'export GRADLE_HOME=~/gradle-8.0.2' >> ~/.bashrc
echo 'export PATH=$PATH:$GRADLE_HOME/bin' >> ~/.bashrc
source ~/.bashrc

# 验证
gradle -version
```

### 4. 安装 Android SDK（精简版）

```bash
# 创建 SDK 目录
mkdir -p ~/android-sdk

# 下载 SDK Command Line Tools
cd ~/android-sdk
wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip

# 解压
unzip commandlinetools-linux-9477386_latest.zip -d cmdline-tools
mv cmdline-tools/cmdline-tools cmdline-tools/latest

# 配置环境变量
echo 'export ANDROID_HOME=~/android-sdk' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin' >> ~/.bashrc
source ~/.bashrc

# 安装必要的 SDK 组件
sdkmanager "platform-tools" "platforms;android-33" "build-tools;33.0.0"
```

### 5. 复制项目到 Termux

```bash
# 创建工作目录
cd ~
mkdir projects

# 从 root 目录复制项目（需要 root 权限）
su -c "cp -r /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat ~/projects/"

# 或者如果你已经把项目复制到 /sdcard/
cp -r /sdcard/BeautifulWeChat ~/projects/

cd ~/projects/BeautifulWeChat
```

### 6. 编译项目

```bash
# 确保 gradlew 有执行权限
chmod +x gradlew

# 开始编译
./gradlew assembleRelease

# 等待编译完成...
# 编译可能需要 10-30 分钟（首次编译会下载依赖）
```

### 7. 安装 APK

```bash
# 编译成功后，APK 在：
# app/build/outputs/apk/release/app-release.apk

# 复制到 /sdcard 方便安装
cp app/build/outputs/apk/release/app-release.apk /sdcard/Download/

# 然后在文件管理器中找到并安装
```

---

## ⚡ 一键脚本（适合 Termux）

将以下内容保存为 `setup_and_build.sh`：

```bash
#!/data/data/com.termux/files/usr/bin/bash

echo "=========================================="
echo "Beautiful WeChat - Termux 编译脚本"
echo "=========================================="

# 检查 Termux 环境
if [ ! -d "$PREFIX" ]; then
    echo "❌ 错误：请在 Termux 中运行此脚本"
    exit 1
fi

# 安装依赖
echo "📦 安装依赖..."
pkg update -y
pkg install openjdk-17 git wget unzip -y

# 设置 Gradle
if [ ! -d "$HOME/gradle-8.0.2" ]; then
    echo "📥 下载 Gradle..."
    cd ~
    wget https://services.gradle.org/distributions/gradle-8.0.2-bin.zip
    unzip -q gradle-8.0.2-bin.zip
    echo 'export GRADLE_HOME=~/gradle-8.0.2' >> ~/.bashrc
    echo 'export PATH=$PATH:$GRADLE_HOME/bin' >> ~/.bashrc
fi

# 设置 Android SDK
if [ ! -d "$HOME/android-sdk" ]; then
    echo "📥 下载 Android SDK..."
    mkdir -p ~/android-sdk
    cd ~/android-sdk
    wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
    unzip -q commandlinetools-linux-9477386_latest.zip -d cmdline-tools
    mv cmdline-tools/cmdline-tools cmdline-tools/latest
    
    echo 'export ANDROID_HOME=~/android-sdk' >> ~/.bashrc
    echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin' >> ~/.bashrc
    source ~/.bashrc
    
    yes | sdkmanager "platform-tools" "platforms;android-33" "build-tools;33.0.0"
fi

# 准备项目
echo "📁 准备项目..."
cd ~
mkdir -p projects
cd projects

if [ -d "BeautifulWeChat" ]; then
    cd BeautifulWeChat
else
    echo "❌ 错误：项目目录不存在"
    echo "请先将项目复制到 ~/projects/BeautifulWeChat"
    exit 1
fi

# 编译
echo "🔨 开始编译..."
chmod +x gradlew
./gradlew clean assembleRelease

if [ $? -eq 0 ]; then
    echo "✅ 编译成功！"
    cp app/build/outputs/apk/release/app-release.apk /sdcard/Download/BeautifulWeChat.apk
    echo "📱 APK 已复制到：/sdcard/Download/BeautifulWeChat.apk"
    echo "请在文件管理器中找到并安装"
else
    echo "❌ 编译失败，请查看错误日志"
    exit 1
fi
```

使用方法：
```bash
chmod +x setup_and_build.sh
./setup_and_build.sh
```

---

## 🐛 常见问题

### Q: Termux 中下载太慢？
A: 更换镜像源：
```bash
# 编辑源文件
nano $PREFIX/etc/apt/sources.list

# 替换为清华源
deb https://mirrors.tuna.tsinghua.edu.cn/termux/apt/termux-main stable main

# 保存退出（Ctrl+X, Y, Enter）
pkg update
```

### Q: 内存不足？
A: 编译需要至少 4GB 可用内存，可以：
1. 关闭其他应用释放内存
2. 使用 swap（需要 root）
3. 使用电脑编译（推荐）

### Q: 编译超时？
A: 首次编译会下载很多依赖，可能需要 30 分钟以上，确保：
1. 网络稳定
2. 存储空间充足（至少 5GB）
3. 保持屏幕常亮

### Q: Gradle 下载失败？
A: 使用阿里云镜像，编辑 `build.gradle`：
```gradle
allprojects {
    repositories {
        maven { url 'https://maven.aliyun.com/repository/google' }
        maven { url 'https://maven.aliyun.com/repository/public' }
        google()
        mavenCentral()
    }
}
```

---

## 📊 资源需求

- **存储空间**: 至少 5GB
- **内存**: 至少 4GB RAM
- **网络**: 稳定的网络连接
- **时间**: 首次编译 20-40 分钟

---

## 🎯 推荐方案对比

| 方案 | 难度 | 时间 | 推荐度 |
|------|------|------|--------|
| PC 编译 | ⭐ 简单 | 5 分钟 | ⭐⭐⭐⭐⭐ |
| Termux 编译 | ⭐⭐⭐ 中等 | 30+ 分钟 | ⭐⭐⭐ |
| 在线编译 | ⭐⭐ 简单 | 10 分钟 | ⭐⭐⭐⭐ |

**最推荐**：如果有电脑，用电脑编译最快最稳定。

如果只有手机，Termux 可行但费时费力，建议找朋友借电脑编译一次即可。

---

需要更详细的帮助？查看 `COMPILE_GUIDE.md`
