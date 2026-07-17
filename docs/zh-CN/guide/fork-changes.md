# Fork 变更与维护

## 概述

本 Fork（`AGAGAG666/LeviLaunchroid`）基于 [LiteLDev/LeviLaunchroid](https://github.com/LiteLDev/LeviLaunchroid)，主要修改包括：
- 移除 Google Play 商店验证
- 移除强制 Play Store 版本检查
- 移除强制版本隔离
- 新增内存编辑器
- 存储迁移系统留空

## Fork 定制功能

### 内存编辑器

设置中可开启完整的内存编辑器，包含：
- `MemoryEditorButton.java` — 设置开关，打开内存编辑器面板
- `MemoryEditorOverlay.java` — 为已保存地址配置命名悬浮按钮
- `MemoryOverlayButton.java` — 可拖动的悬浮按钮，应用/恢复内存值
- `MemorySearchEngine.java`、`MemoryAddress.java`、`MemoryEditorNative.java` — 原生内存读写/搜索
- `SavedAddressManager.java`、`FreezeManager.java` — 持久化地址与冻结

所有文件位于 `app/src/main/java/org/levimc/launcher/core/mods/memoryeditor/`。

引用位置：
- `MainActivity.java` — 设置开关
- `InbuiltOverlayManager.java` — `addMemoryOverlay()` / `removeMemoryOverlay()` 管理悬浮按钮
- `MemorySavedAdapter.java` — 从已保存列表切换/删除悬浮按钮

### 移除了 Google Play 验证

- `MinecraftPlayStoreValidator.java` — 始终返回合法结果
- Play Store 弹窗与导航已禁用
- Play Store 合规字符串存在但永不触发

### 移除了包名锁定

- `PackageNameLockManager.java` — 始终通过

### 移除了强制版本隔离

- 版本隔离改为设置中可选，永不强制
- 相关弹窗仅在用户手动启用时出现

### 存储迁移留空

- `StorageMigrationManager.java` — 空类，方法无操作
- `MainActivity.java` — `showStorageMigrationDialog()` 无操作
- 迁移相关 XML 布局存在但永不触发
- `storage_migration_*` 翻译字符串均为占位值

### 移除了 SQLCipher 依赖

- `net.zetetic:android-database-sqlcipher` 已从 `build.gradle` 移除
- 所有数据库操作使用原生 `SQLiteDatabase`

### CurseForge 集成

- 完整的 CurseForge 浏览/下载功能（fork 特性，合并前已存在）

## 上游合并记录（2026 年 7 月）

### 来源

- **上游**: `LiteLDev/LeviLaunchroid` 分支 `main` 提交 `a3bcf91`
- **目标**: Fork 分支 `test` 提交 `c8f6e6f`

### 合并策略

合并提交 `ca0bb21`：
- `.github/workflows/docs.yml` 取 **ours**（fork 已移除 docs CI）
- `InbuiltOverlayManager.java` 取 **upstream**（新增 `HudOverlay`、`ExternalButtonOverlay`、模组菜单覆盖系统；fork 的内存编辑器覆盖独立运行）
- `MainActivity.java` 取 **ours**（保留 fork 的 `showStorageMigrationDialog()` 空实现）
- `StorageMigrationManager.java` 取 **ours**（保留空类）
- `strings.xml` 取 **both**（保留上游迁移字符串 + fork 既有字符串）

### 冲突解决

| 文件 | 方案 |
|------|------|
| `.github/workflows/docs.yml` | 删除（fork 已移除） |
| `InbuiltOverlayManager.java` | 使用上游版本，后补回 `addMemoryOverlay()` / `removeMemoryOverlay()` |
| `MainActivity.java` | 保留 fork 的迁移空实现 |
| `StorageMigrationManager.java` | 保留 fork 的空类 |
| `strings.xml`（所有语言） | 保留双方；后移除缺少 `<string name="">` 标签的孤立片段 |

### 构建修复

1. **SIGBUS 崩溃修复**（`MinecraftRuntimePreparer.kt:82`）：注释掉 `nativeSetupRuntime(...)` — 原生 `libgxcore.so` 在解析 `/proc/self/maps` 时遇到 binderfs 映射崩溃。
2. **孤立的 XML 片段**：9 个语言的 `strings.xml` 文件中移除了缺少起始标签的 `</string>`。
3. **缺失的覆盖方法**：向 `InbuiltOverlayManager.java` 补回 `addMemoryOverlay(MemoryAddress)` 和 `removeMemoryOverlay(long)`。

### 合并后修改

| 提交 | 变更 |
|------|------|
| `fd89bd4` | 注释掉 `MinecraftRuntimePreparer.kt` 中的 `nativeSetupRuntime()` |
| `ca0bb21` | 合并提交 |
| `1522282` | 修复 5 个语言文件中的孤立 XML |
| `7a3cbc1` | 修复另外 4 个语言文件中的孤立 XML |
| `524087d` | 恢复 `InbuiltOverlayManager.java` 中的 `addMemoryOverlay`/`removeMemoryOverlay` |

## 如何再次合并上游

### 步骤

```bash
# 1. 获取最新上游
git remote add upstream https://github.com/LiteLDev/LeviLaunchroid.git
git fetch upstream

# 2. 在 test 分支开始合并
git checkout test
git merge upstream/main --no-ff

# 3. 解决冲突
#    - 保留 fork 的空实现：StorageMigrationManager、MinecraftPlayStoreValidator、
#      PackageNameLockManager、MainActivity 迁移弹窗
#    - InbuiltOverlayManager.java：取上游，再补回内存覆盖方法
#    - strings.xml：保留双方，然后检查孤立片段

# 4. 解决后验证内存编辑器仍可编译
#    grep -rn 'addMemoryOverlay\|removeMemoryOverlay' app/src/main/java/

# 5. 推送并等待 CI 验证
git push origin test
```

### 需关注的文件

- `InbuiltOverlayManager.java` — 上游重构可能丢弃 `addMemoryOverlay`/`removeMemoryOverlay`
- `strings.xml` — 迁移字符串可能被加回；fork 将其作为占位符
- `build.gradle` — SQLCipher 依赖可能重新出现
- `MainActivity.java` — 迁移/Play Store 弹窗可能被重新加入
- `memoryeditor/` 下的所有文件 — 仅 fork 拥有，需确保能编译

## 附录：分析标准

所有调试、排查和维护工作均应遵守[操作指南](operation-guide.md)。该指南定义了：

- **分析范围边界** — 哪些属于分析范围，哪些不是
- **证据优先级** — 运行时行为优先于静态代码，网络流量优先于注释
- **标准流程** — 先被动侦察、运行时追踪、窄路径验证
- **可复现性要求** — 每项发现必须能在干净基线环境中独立验证

详细内容请参阅[完整文档](operation-guide.md)。
