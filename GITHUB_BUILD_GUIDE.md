# GitHub Actions 自动编译指南

## 已完成配置

✅ GitHub Actions 工作流已配置
✅ Gradle 配置文件已修复
✅ AndroidX 支持已启用

## 使用步骤

### 1. 创建 GitHub 仓库

在手机浏览器访问：https://github.com/new

- 仓库名：BeautifulWeChat
- 选择 Public（免费）
- 不勾选任何初始化选项
- 点击 "Create repository"

### 2. 上传项目

#### 方法 A：使用 GitHub 网页（最简单）

1. 在新建的仓库页面，点击 "uploading an existing file"
2. 将整个 BeautifulWeChat 文件夹的内容选中上传
3. 确保包含 `.github/workflows/build.yml` 文件
4. 在底部填写提交信息："Initial commit"
5. 点击 "Commit changes"

#### 方法 B：使用 Git 命令（需要终端）

```bash
cd /storage/emulated/0/BeautifulWeChat
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/你的用户名/BeautifulWeChat.git
git push -u origin main
```

### 3. 触发编译

上传完成后：

1. 进入仓库的 "Actions" 标签页
2. 如果看到工作流，点击 "I understand my workflows, go ahead and enable them"
3. 点击左侧的 "Build APK" 工作流
4. 点击右上角 "Run workflow" → "Run workflow"

### 4. 等待编译

- 编译时间：5-10 分钟
- 可以实时查看编译日志
- 成功后会显示绿色对勾

### 5. 下载 APK

编译成功后：

1. 点击完成的工作流运行
2. 下拉到 "Artifacts" 部分
3. 点击 "BeautifulWeChat-release" 下载
4. 解压 zip 文件得到 APK

## 项目文件清单

确保上传时包含以下文件：

```
BeautifulWeChat/
├── .github/
│   └── workflows/
│       └── build.yml          ← GitHub Actions 配置
├── app/                       ← 应用源码
├── gradle/                    ← Gradle wrapper
├── build.gradle              ← 项目构建配置（已修复）
├── settings.gradle           ← 项目设置（已修复）
├── gradle.properties         ← Gradle 属性（已添加）
└── gradlew                   ← Gradle 脚本
```

## 常见问题

### Q: 编译失败怎么办？

1. 点击失败的工作流查看日志
2. 检查是否所有文件都上传完整
3. 确认 `.github/workflows/build.yml` 存在

### Q: 找不到 Artifacts？

编译必须成功（绿色对勾）才会有 Artifacts。失败时不会生成 APK。

### Q: 可以修改代码后重新编译吗？

可以！修改代码后：
- 网页上传：直接上传修改的文件
- Git：commit + push 即可自动触发新的编译

## 提示

- GitHub Actions 每月有 2000 分钟免费额度（公开仓库无限）
- 每次编译约消耗 5-10 分钟
- 保持仓库为 Public 可享受无限制编译

---

配置完成日期：2026-11-03
所有配置文件已就绪，可直接上传使用。
