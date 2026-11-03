#!/bin/bash

# Beautiful WeChat 编译脚本

echo "=========================================="
echo "Beautiful WeChat - LSPosed Module Build"
echo "=========================================="
echo ""

# 检查 Java 环境
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未检测到 Java。请先安装 Java JDK 11 或更高版本。"
    exit 1
fi

echo "✓ Java 版本: $(java -version 2>&1 | grep version)"
echo ""

# 创建 gradlew（如果不存在）
if [ ! -f "gradlew" ]; then
    echo "⚙️  生成 gradle wrapper..."
    # 使用 gradle 命令或者 gradle wrapper 插件
    gradle wrapper --gradle-version 8.0.2 --distribution-type all || {
        echo "⚠️  警告: gradle wrapper 生成失败，将使用系统 gradle"
    }
fi

# 编译
echo ""
echo "🔨 开始编译..."
./gradlew clean assembleRelease

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ 编译成功！"
    echo ""
    APK_PATH="app/build/outputs/apk/release/app-release.apk"
    if [ -f "$APK_PATH" ]; then
        APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
        echo "📦 APK 文件："
        echo "   路径: $APK_PATH"
        echo "   大小: $APK_SIZE"
        echo ""
        echo "📱 安装方法："
        echo "   1. 将 APK 发送到手机或使用 adb 安装"
        echo "   2. 在 LSPosed 中启用该模块"
        echo "   3. 将微信 (com.tencent.mm) 加入作用域"
        echo "   4. 重启微信"
    fi
else
    echo ""
    echo "❌ 编译失败！"
    echo "   请检查错误日志"
    exit 1
fi

echo ""
echo "=========================================="
