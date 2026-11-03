# ⚡ MT 管理器编译 - 30 秒快速开始

## 三步完成

### 1️⃣ 打开 MT 终端

- MT 管理器
- 左下 **≡** → **终端模拟器**

### 2️⃣ 获取 root 并编译

在终端中逐行复制粘贴：

```bash
su
```

选择 **允许**

```bash
cd /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat && chmod +x gradlew && ./gradlew assembleRelease
```

⏳ **等待 20-40 分钟...**

看到 `BUILD SUCCESSFUL` 说明成功

### 3️⃣ 获取 APK

```bash
cp app/build/outputs/apk/release/app-release.apk /sdcard/Download/ && echo "✅ APK 已复制到 /sdcard/Download/"
```

---

## 🎉 完成

- 去 `/sdcard/Download/` 找到 APK
- 点击安装
- LSPosed 中启用模块
- 重启微信

---

**重要**：确保已安装 Termux 并执行过 `pkg install openjdk-17 -y`

遇到 "java: command not found" 就去装 Termux 的 Java

---

需要完整指南？→ 看 `MT_TERMINAL_GUIDE.md`
