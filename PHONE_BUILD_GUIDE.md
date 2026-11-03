# 🎯 在手机上编译的终极指南

我理解你的困境。如果电脑一直处理不好，这是最实用的方案。

---

## ⚡ 5 分钟快速开始方案

### 步骤 1：打开手机浏览器

在 Chrome 或其他浏览器中访问：
```
https://github.com
```

### 步骤 2：注册或登录

- 点击 "Sign up"
- 输入邮箱、密码、用户名
- 验证邮箱

### 步骤 3：创建项目仓库

1. 点击右上角 "+"
2. 选择 "New repository"
3. 仓库名输入：`BeautifulWeChat`
4. 选择 "Public"（免费）
5. 勾选 "Add a README file"
6. 点击 "Create repository"

### 步骤 4：上传项目文件

1. 进入新仓库
2. 点击 "Add file" → "Upload files"
3. 将 BeautifulWeChat 文件夹中的所有文件拖拽到浏览器
   - 先上传 `build.gradle`, `settings.gradle` 等根文件
   - 然后上传 `app/` 文件夹

**或者使用压缩文件**（更简单）：
1. 在手机文件管理器中压缩 BeautifulWeChat 文件夹为 ZIP
2. 上传 ZIP 文件
3. GitHub 会自动解压

### 步骤 5：创建自动编译配置

1. 回到仓库主页
2. 找到 "Create new file" 或点击 "Add file"
3. 输入文件名：`.github/workflows/build.yml`
4. 粘贴以下内容：

```yaml
name: Build APK

on:
  push:
    branches: [ main, master ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'adopt'
        
    - name: Make gradlew executable
      run: chmod +x gradlew
      
    - name: Build Release APK
      run: ./gradlew assembleRelease
      
    - name: Sign APK
      uses: r0adkll/sign-android-release@v1
      with:
        releaseDirectory: app/build/outputs/apk/release
        signingKeyBase64: ${{ secrets.SIGNING_KEY }}
        alias: ${{ secrets.ALIAS }}
        keyStorePassword: ${{ secrets.KEY_STORE_PASSWORD }}
        keyPassword: ${{ secrets.KEY_PASSWORD }}
      env:
        BUILD_TOOLS_VERSION: "33.0.0"
        
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-release
        path: app/build/outputs/apk/release/*.apk
```

5. 点击 "Commit changes"

### 步骤 6：运行编译

1. 点击 "Actions" 标签页
2. 点击左边工作流 "Build APK"
3. 点击 "Run workflow" → "Run workflow"
4. 等待 5-10 分钟

### 步骤 7：下载 APK

1. 编译完成后（会显示绿色对勾）
2. 点击工作流运行
3. 找到 "Artifacts" 部分
4. 点击 "app-release" 下载
5. 解压获得 APK

---

## 📱 手机上详细操作截图步骤

### 无法显示截图，但流程是这样的：

**访问 GitHub**
```
Chrome 地址栏 → github.com
```

**登录/注册**
```
屏幕右上角 Sign in / Sign up
```

**创建仓库**
```
右上角 + 号 → New repository
输入名称 → Public → Create
```

**上传文件**
```
绿色 Code 按钮 → Add file → Upload files
拖拽文件 → Commit
```

**设置自动编译**
```
Create new file → .github/workflows/build.yml
粘贴工作流代码 → Commit
```

**运行编译**
```
Actions 标签 → Build APK → Run workflow
等待 5-10 分钟 → 下载 APK
```

---

## ✅ 验证编译成功

当你看到：
- ✅ Actions 标签页中有绿色对勾
- ✅ 工作流显示 "completed successfully"
- ✅ 有 Artifacts 可下载
- ✅ APK 文件大小 > 100KB

就说明编译成功了！

---

## 🎯 下载后如何安装？

### 方法 1：直接安装（推荐）
1. 在手机浏览器中下载 APK
2. 点击下载完成的文件
3. 允许安装未知来源
4. 安装

### 方法 2：使用文件管理器
1. 下载 APK 到 /sdcard/Download/
2. 打开文件管理器
3. 找到 APK 文件
4. 双击安装

---

## 🔧 如果编译失败

**常见错误和解决方案**

### 错误 1：Gradle 下载失败
**解决**：编辑 `build.gradle` 文件，添加镜像源

在 Actions 中修改 `build.gradle`：
```gradle
repositories {
    maven { url 'https://maven.aliyun.com/repository/google' }
    maven { url 'https://maven.aliyun.com/repository/public' }
    google()
    mavenCentral()
}
```

### 错误 2：编译超时
**解决**：GitHub Actions 有免费额度限制，重新运行工作流

### 错误 3：签名错误
**解决**：使用 debug 版本（不需要签名）

修改工作流中的编译命令：
```yaml
run: ./gradlew assembleDebug
```

---

## 💡 如果你更喜欢 Termux 本地编译

### 在 Termux 中一行一行执行

```bash
# 1. 更新
pkg update -y && pkg upgrade -y

# 2. 安装 Java
pkg install openjdk-17 -y

# 3. 验证 Java
java -version

# 4. 如果上一步成功，继续...

# 5. 进入项目目录
cd /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat

# 6. 给 gradlew 执行权限
chmod +x gradlew

# 7. 开始编译（这会花 20-40 分钟）
./gradlew assembleRelease

# 8. 完成后，APK 在：
# app/build/outputs/apk/release/app-release.apk
```

**需要耐心和足够的手机存储空间（5GB+）**

---

## ⚠️ 常见问题

### Q: GitHub 需要费用吗？
**A**: 完全免费，包括 Actions 编译时间。

### Q: 编译需要多久？
**A**: 首次 10-15 分钟（下载依赖），之后 5-10 分钟。

### Q: 编译失败了怎么办？
**A**: 查看 Actions 日志，我可以帮你调试。

### Q: Termux 和 GitHub 哪个更好？
**A**: 
- GitHub：更简单，不需要下载工具
- Termux：学习价值更高

### Q: APK 安装后会自动启用吗？
**A**: 不会，需要在 LSPosed 中手动启用。

---

## 🚀 我推荐的流程

1. **GitHub Actions**（最简单）
   - 在手机浏览器操作
   - 10 分钟完成
   - 自动编译

2. **或者 Termux**（更学习化）
   - 学习命令行
   - 20 分钟完成
   - 完全本地

3. **如果都不行**
   - 找任何人帮忙
   - 给他们这个项目 ZIP
   - 他们一行命令完成

---

## 📞 需要帮助？

**告诉我**：
- [ ] 我想用 GitHub（给我详细步骤）
- [ ] 我想用 Termux（教我如何操作）
- [ ] 我想用其他方法（说说你的想法）

**我会立即给你最详细的指导！**

---

**关键点**：
✅ 完全免费
✅ 在手机上操作
✅ 自动化编译
✅ 15 分钟完成

你选哪个方案？
