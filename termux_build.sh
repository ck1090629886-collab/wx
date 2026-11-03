#!/data/data/com.termux/files/usr/bin/bash

# Beautiful WeChat - Termux 一键编译脚本
# 使用方法：bash termux_build.sh

echo "=========================================="
echo "Beautiful WeChat - Termux 编译脚本"
echo "=========================================="
echo ""

# 检查是否在 Termux 环境
if [ ! -d "$PREFIX" ]; then
    echo "❌ 错误：请在 Termux 中运行此脚本"
    exit 1
fi

echo "✓ Termux 环境检测通过"
echo ""

# 第 1 步：更新软件包
echo "📦 步骤 1/6：更新软件包..."
pkg update -y && pkg upgrade -y
if [ $? -ne 0 ]; then
    echo "⚠️  软件包更新失败，但继续尝试..."
fi
echo "✓ 软件包更新完成"
echo ""

# 第 2 步：安装 Java JDK
echo "☕ 步骤 2/6：安装 Java JDK 17..."
pkg install openjdk-17 -y
if [ $? -ne 0 ]; then
    echo "❌ Java 安装失败"
    exit 1
fi

# 验证 Java 安装
java -version
if [ $? -ne 0 ]; then
    echo "❌ Java 验证失败"
    exit 1
fi
echo "✓ Java JDK 17 安装成功"
echo ""

# 第 3 步：安装必要工具
echo "🔧 步骤 3/6：安装必要工具..."
pkg install wget unzip -y
echo "✓ 工具安装完成"
echo ""

# 第 4 步：复制项目到 Termux 可访问的位置
echo "📁 步骤 4/6：准备项目文件..."

# 检查项目是否在 /sdcard
if [ -d "/sdcard/BeautifulWeChat" ]; then
    echo "✓ 在 /sdcard 中找到项目"
    PROJECT_DIR="/sdcard/BeautifulWeChat"
elif [ -d "/data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat" ]; then
    echo "✓ 在原始位置找到项目"
    # 复制到 Termux 可访问的位置
    echo "正在复制项目到 Termux..."
    
    # 需要 root 权限复制
    if [ "$(id -u)" -eq 0 ]; then
        cp -r "/data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat" "$HOME/"
        PROJECT_DIR="$HOME/BeautifulWeChat"
    else
        echo "⚠️  需要 root 权限复制项目"
        echo "请手动复制项目到 /sdcard/BeautifulWeChat"
        echo "或者使用文件管理器复制"
        exit 1
    fi
else
    echo "❌ 找不到项目文件"
    echo "请将项目复制到以下位置之一："
    echo "  - /sdcard/BeautifulWeChat"
    echo "  - $HOME/BeautifulWeChat"
    exit 1
fi

cd "$PROJECT_DIR" || exit 1
echo "✓ 项目目录：$PROJECT_DIR"
echo ""

# 第 5 步：配置 Android SDK（精简版）
echo "📲 步骤 5/6：配置 Android SDK..."
if [ ! -d "$HOME/android-sdk" ]; then
    echo "正在下载 Android SDK Command Line Tools..."
    mkdir -p "$HOME/android-sdk"
    cd "$HOME/android-sdk"
    
    # 下载 SDK
    wget -q --show-progress https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
    
    if [ $? -ne 0 ]; then
        echo "❌ SDK 下载失败，可能是网络问题"
        echo "请检查网络连接或使用国内镜像"
        exit 1
    fi
    
    unzip -q commandlinetools-linux-9477386_latest.zip -d cmdline-tools
    mv cmdline-tools/cmdline-tools cmdline-tools/latest
    rm commandlinetools-linux-9477386_latest.zip
    
    # 配置环境变量
    export ANDROID_HOME="$HOME/android-sdk"
    export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin"
    
    # 写入配置文件
    echo "export ANDROID_HOME=$HOME/android-sdk" >> "$HOME/.bashrc"
    echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin' >> "$HOME/.bashrc"
    
    echo "✓ Android SDK 下载完成"
    
    # 安装必要的 SDK 组件
    echo "正在安装 SDK 组件（需要几分钟）..."
    yes | sdkmanager "platform-tools" "platforms;android-33" "build-tools;33.0.0" > /dev/null 2>&1
    
    echo "✓ Android SDK 配置完成"
else
    echo "✓ Android SDK 已存在，跳过下载"
    export ANDROID_HOME="$HOME/android-sdk"
    export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin"
fi
echo ""

# 回到项目目录
cd "$PROJECT_DIR" || exit 1

# 第 6 步：编译项目
echo "🔨 步骤 6/6：开始编译..."
echo "⏳ 这可能需要 20-40 分钟，请耐心等待..."
echo ""

# 给 gradlew 执行权限
chmod +x gradlew

# 开始编译
echo "执行命令：./gradlew assembleRelease"
echo ""
./gradlew assembleRelease --stacktrace

# 检查编译结果
if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✅ 编译成功！"
    echo "=========================================="
    echo ""
    
    APK_PATH="app/build/outputs/apk/release/app-release.apk"
    
    if [ -f "$APK_PATH" ]; then
        APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
        echo "📦 APK 信息："
        echo "   文件：$APK_PATH"
        echo "   大小：$APK_SIZE"
        echo ""
        
        # 复制到方便访问的位置
        cp "$APK_PATH" /sdcard/Download/BeautifulWeChat.apk
        echo "✓ APK 已复制到：/sdcard/Download/BeautifulWeChat.apk"
        echo ""
        echo "📱 下一步："
        echo "1. 使用文件管理器打开 /sdcard/Download/"
        echo "2. 找到 BeautifulWeChat.apk"
        echo "3. 点击安装"
        echo "4. 在 LSPosed 中启用模块"
        echo "5. 将微信加入作用域"
        echo "6. 重启微信查看效果"
    else
        echo "⚠️  APK 文件未找到，但编译似乎成功了"
        echo "请检查：$APK_PATH"
    fi
else
    echo ""
    echo "=========================================="
    echo "❌ 编译失败"
    echo "=========================================="
    echo ""
    echo "请检查以上错误信息"
    echo "常见问题："
    echo "1. 内存不足 - 关闭其他应用"
    echo "2. 存储空间不足 - 至少需要 5GB"
    echo "3. 网络问题 - 下载依赖失败"
    echo ""
    echo "如需帮助，请将错误信息截图"
    exit 1
fi

echo ""
echo "=========================================="
echo "🎉 全部完成！"
echo "=========================================="
