# 🎯 Termux 编译 - 快速版（复制即用）

## 第 1 步：打开 Termux

在手机上打开 Termux 应用（黑色窗口）

---

## 第 2 步：逐块复制粘贴命令

### 块 1：准备环境（约 5 分钟）

复制以下全部内容，粘贴到 Termux 后按 Enter：

```bash
pkg update && pkg upgrade -y && pkg install openjdk-17 wget unzip git -y && java -version
```

**等待完成，看到 "java version" 输出说明成功**

---

### 块 2：进入项目（即时）

```bash
cd /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat
```

---

### 块 3：准备编译（即时）

```bash
chmod +x gradlew
```

---

### 块 4：开始编译（20-40 分钟）⏳

```bash
./gradlew assembleRelease --stacktrace
```

**关键点**：
- 这一步会输出很多文本（正常的）
- 会花 20-40 分钟
- **不要中断！**
- 最后会显示 `BUILD SUCCESSFUL` 和 `26 actionable tasks: 26 executed`

---

### 块 5：获取 APK（即时）

编译成功后，执行：

```bash
cp app/build/outputs/apk/release/app-release.apk /sdcard/Download/ && ls -lh /sdcard/Download/app-release.apk
```

**看到下面的输出说明成功了**：
```
-rw-r--r-- 1 root root 450K ... /sdcard/Download/app-release.apk
```

---

## 第 3 步：安装 APK

1. 打开文件管理器（如 MT 管理器）
2. 进入 `/sdcard/Download/`
3. 找到 `app-release.apk`
4. 点击 → 安装

---

## 第 4 步：在 LSPosed 中激活

1. 打开 **LSPosed 管理器**
2. **模块** 标签 → 找到 **Beautiful WeChat**
3. 打开开关（变蓝）
4. 点击模块名 → **作用域** → 勾选 **微信**
5. 保存

---

## 第 5 步：重启微信

- 微信 → 设置 → 强制停止
- 重新打开微信
- 进入任意聊天查看效果

---

## ✅ 完成！

你应该看到：
- 发送气泡：🟣 蓝紫渐变
- 接收气泡：🟢 黄绿渐变
- 白色文字
- 圆角气泡

---

## 🆘 如果出问题

### 问题：命令无法执行

**解决**：试试分开执行
```bash
pkg update
pkg upgrade -y
pkg install openjdk-17 -y
java -version
```

### 问题：编译中卡住或出错

**解决**：重试编译
```bash
cd /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat
./gradlew assembleRelease --stacktrace
```

### 问题：找不到项目

**解决**：先复制项目
```bash
# 在你这个终端中不用做，项目已在正确位置
# 如果真的找不到，可以试
ls -la /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat
```

### 问题：APK 没生成

**解决**：检查编译结果
```bash
ls -la /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat/app/build/outputs/apk/release/
```

---

## 📞 现在就开始！

1. ✅ 打开 Termux
2. ✅ 复制第一块命令
3. ✅ 等待完成
4. ✅ 继续下一块

**如果卡在某一步，告诉我你看到的错误信息！**

我会帮你解决 ✨
