# 🖥️ PC 端编译指南 - Beautiful WeChat

由于 Android 设备上没有完整的 Java 开发环境，建议在电脑上编译本项目。

## 方法一：Windows 系统编译（推荐）

### 1. 安装必要工具

#### 下载并安装 Android Studio
1. 访问：https://developer.android.com/studio
2. 下载最新版本
3. 安装时选择 "Standard" 安装类型
4. 等待自动下载 SDK 和工具

#### 或者只安装 Android SDK（轻量级）
1. 访问：https://developer.android.com/studio#command-tools
2. 下载 "Command line tools only"
3. 解压到 `C:\Android\`
4. 配置环境变量：
   ```
   ANDROID_HOME=C:\Android\
   Path 添加：%ANDROID_HOME%\cmdline-tools\latest\bin
   ```

### 2. 将项目传输到 PC

#### 方法 A：USB 连接
```bash
# 在手机上找到项目目录
# /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat/

# 使用 adb pull 拉取到电脑
adb pull /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat/ C:\Projects\
```

#### 方法 B：文件管理器
1. 使用 MT 管理器或其他文件管理器
2. 将 BeautifulWeChat 文件夹压缩为 zip
3. 复制到 /sdcard/Download/
4. 通过 USB 传输到电脑

#### 方法 C：云盘同步
1. 压缩 BeautifulWeChat 文件夹
2. 上传到百度云盘/OneDrive
3. 在电脑上下载

### 3. 在电脑上编译

#### 使用 Android Studio（GUI 方式）
1. 打开 Android Studio
2. File → Open → 选择 BeautifulWeChat 文件夹
3. 等待 Gradle 同步完成
4. Build → Build Bundle(s) / APK(s) → Build APK(s)
5. 编译完成后，APK 在 `app/build/outputs/apk/release/`

#### 使用命令行（推荐）
```bash
# 进入项目目录
cd C:\Projects\BeautifulWeChat

# 确保 gradlew 有执行权限（Linux/Mac）
chmod +x gradlew

# Windows 直接运行
gradlew.bat assembleRelease

# Linux/Mac 运行
./gradlew assembleRelease

# 编译完成后
# APK 位置：app\build\outputs\apk\release\app-release.apk
```

### 4. 安装到手机

```bash
# 确认手机已连接并开启 USB 调试
adb devices

# 安装 APK
adb install app\build\outputs\apk\release\app-release.apk
```

---

## 方法二：Linux/Mac 编译

### 1. 安装 Java JDK

#### Ubuntu/Debian
```bash
sudo apt update
sudo apt install openjdk-11-jdk
java -version  # 验证安装
```

#### macOS
```bash
brew install openjdk@11
```

### 2. 安装 Android SDK

```bash
# 下载 SDK command line tools
wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip

# 解压
unzip commandlinetools-linux-9477386_latest.zip -d ~/android-sdk

# 配置环境变量
export ANDROID_HOME=~/android-sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
```

### 3. 传输项目并编译

```bash
# 从手机拉取项目
adb pull /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat/ ~/Projects/

# 进入目录
cd ~/Projects/BeautifulWeChat

# 编译
./gradlew assembleRelease

# 安装
adb install app/build/outputs/apk/release/app-release.apk
```

---

## 方法三：在线编译（无需本地环境）

### 使用 GitHub Actions（免费）

1. **创建 GitHub 仓库**
   - 访问 github.com
   - 创建新仓库
   - 上传 BeautifulWeChat 项目

2. **创建 GitHub Actions 工作流**

在项目根目录创建 `.github/workflows/build.yml`：

```yaml
name: Build APK

on:
  push:
    branches: [ main ]
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
        distribution: 'temurin'
        
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Build with Gradle
      run: ./gradlew assembleRelease
      
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-release
        path: app/build/outputs/apk/release/app-release.apk
```

3. **触发编译**
   - 推送代码到 GitHub
   - 在 Actions 标签页查看编译进度
   - 编译完成后下载 APK

---

## 常见问题

### Q: gradlew 命令找不到？
A: 如果项目中没有 gradlew 文件，运行：
```bash
gradle wrapper --gradle-version 8.0.2
```

### Q: Gradle 同步失败？
A: 可能是网络问题，编辑 `build.gradle` 添加阿里云镜像：
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

### Q: 签名错误？
A: Release 版本需要签名，编辑 `app/build.gradle`：
```gradle
android {
    signingConfigs {
        release {
            storeFile file("debug.keystore")
            storePassword "android"
            keyAlias "androiddebugkey"
            keyPassword "android"
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled false
        }
    }
}
```

或使用 debug 版本：
```bash
./gradlew assembleDebug
```

### Q: 编译成功但无法安装？
A: 确认：
1. 手机已开启"允许安装未知来源应用"
2. APK 签名正确
3. 手机架构匹配（本项目支持所有架构）

---

## 完整编译流程示例（Windows）

```cmd
REM 1. 从手机拉取项目
adb pull /data/adb/MilkCode/2026-11-03/你好/work/BeautifulWeChat C:\Projects\

REM 2. 进入项目目录
cd C:\Projects\BeautifulWeChat

REM 3. 清理旧的构建
gradlew.bat clean

REM 4. 编译 Release 版本
gradlew.bat assembleRelease

REM 5. 查看 APK
dir app\build\outputs\apk\release\

REM 6. 安装到手机
adb install -r app\build\outputs\apk\release\app-release.apk
```

---

## 编译后文件位置

```
BeautifulWeChat/
└── app/
    └── build/
        └── outputs/
            └── apk/
                ├── debug/
                │   └── app-debug.apk          (调试版本)
                └── release/
                    └── app-release.apk        (发布版本) ✓
```

---

## 如果你有电脑但不想安装 Android Studio

### 使用 Docker（最简单）

```bash
# 拉取包含 Android SDK 的 Docker 镜像
docker pull mingc/android-build-box

# 运行编译
docker run --rm -v /path/to/BeautifulWeChat:/project mingc/android-build-box bash -c "cd /project && ./gradlew assembleRelease"

# APK 生成在项目目录下
```

---

## 需要帮助？

如果编译过程遇到问题：

1. 查看完整的错误日志
2. 确认 Java 版本（必须 11 或更高）
3. 确认 Android SDK 已正确安装
4. 检查网络连接（Gradle 需要下载依赖）

---

**推荐流程**：手机 → adb pull → PC 编译 → adb install → 完成

编译只需要在 PC 上进行一次，之后的修改和重新编译也都在 PC 上完成。
