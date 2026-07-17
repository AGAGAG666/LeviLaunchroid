# Fork Changes & Maintenance

## Overview

This fork (`AGAGAG666/LeviLaunchroid`) is based on [LiteLDev/LeviLaunchroid](https://github.com/LiteLDev/LeviLaunchroid) with customizations aimed at:
- Removing Google Play Store verification requirements
- Removing forced Play Store version checks
- Removing forced version isolation
- Adding a memory editor feature
- Stubbing out the storage migration system

## Fork Customizations

### Memory Editor

A full memory editor accessible from settings, with:
- `MemoryEditorButton.java` — Toggle in settings, opens the memory editor panel
- `MemoryEditorOverlay.java` — Dialog for configuring named overlay buttons per saved address
- `MemoryOverlayButton.java` — Draggable overlay button that applies/restores memory values
- `MemorySearchEngine.java`, `MemoryAddress.java`, `MemoryEditorNative.java` — Native memory read/write/search
- `SavedAddressManager.java`, `FreezeManager.java` — Persist and freeze address values

All files in `app/src/main/java/org/levimc/launcher/core/mods/memoryeditor/`.

References from:
- `MainActivity.java` — Settings toggle for memory editor
- `InbuiltOverlayManager.java` — `addMemoryOverlay()` / `removeMemoryOverlay()` manage overlay buttons
- `MemorySavedAdapter.java` — Toggle/delete memory overlay buttons from saved addresses list

### Google Play Verification Removed

- `MinecraftPlayStoreValidator.java` — Stubbed out; always returns a valid result
- Play Store dialog and navigation disabled
- Play Store listing compliance strings may still exist but are never triggered

### Package Name Lock Removed

- `PackageNameLockManager.java` — Stubbed out; always passes

### Forced Version Isolation Removed

- Version isolation is opt-in via settings, never forced
- Related dialogs (`dialog_message_version_isolation`) may appear only if user enables it manually

### Storage Migration Stubbed

- `StorageMigrationManager.java` — Empty class with no-op methods
- `MainActivity.java` — `showStorageMigrationDialog()` is a no-op
- Migration-related XML layouts exist but are never triggered
- Translation strings (`storage_migration_*`) are set to placeholder values

### SQLCipher Dependency Removed

- `net.zetetic:android-database-sqlcipher` removed from `build.gradle`
- All database operations use plain `SQLiteDatabase`

### CurseForge Integration

- Full CurseForge browse/download implemented (fork feature, preserved from pre-merge)

## Upstream Merge Log (July 2026)

### Source

- **Upstream**: `LiteLDev/LeviLaunchroid` branch `main` at commit `a3bcf91`
- **Target**: Fork branch `test` at commit `c8f6e6f`

### Merge Strategy

Merge commit `ca0bb21` with:
- **ours** for `.github/workflows/docs.yml` (fork removed docs CI)
- **upstream** for `InbuiltOverlayManager.java` (adds `HudOverlay`, `ExternalButtonOverlay`, mod-menu overlay system; fork's memory editor overlays are independent)
- **ours** for `MainActivity.java` (kept fork's `showStorageMigrationDialog()` stub)
- **ours** for `StorageMigrationManager.java` (kept empty stub)
- **both** for `strings.xml` files (kept upstream migration strings + fork's existing strings)

### Conflicts Resolved

| File | Resolution |
|------|-----------|
| `.github/workflows/docs.yml` | Deleted (fork already removed) |
| `InbuiltOverlayManager.java` | Used upstream version, then re-added `addMemoryOverlay()` / `removeMemoryOverlay()` |
| `MainActivity.java` | Kept fork's migration stub |
| `StorageMigrationManager.java` | Kept fork's empty stub |
| `strings.xml` (all locales) | Kept both; then removed orphaned migration fragments that lost their `<string name="">` tags |

### Build Fixes Applied

1. **SIGBUS Crash Fix** (`MinecraftRuntimePreparer.kt:82`): Commented out `nativeSetupRuntime(...)` call — the native `libgxcore.so` crashed parsing `/proc/self/maps` on binderfs mappings.
2. **Orphaned XML fragments**: Removed dangling `</string>` lines in 9 locale `strings.xml` files that lost their opening tags during merge conflict resolution.
3. **Missing overlay methods**: Re-added `addMemoryOverlay(MemoryAddress)` and `removeMemoryOverlay(long)` to `InbuiltOverlayManager.java`.

### Files Modified Post-Merge

| Commit | Change |
|--------|--------|
| `fd89bd4` | Commented out `nativeSetupRuntime()` in `MinecraftRuntimePreparer.kt` |
| `ca0bb21` | Merge commit |
| `1522282` | Fixed orphaned XML in 5 locale strings files |
| `7a3cbc1` | Fixed orphaned XML in 4 more locale strings files |
| `524087d` | Restored `addMemoryOverlay`/`removeMemoryOverlay` in `InbuiltOverlayManager.java` |

## How to Merge Upstream Again

### Steps

```bash
# 1. Fetch latest upstream
git remote add upstream https://github.com/LiteLDev/LeviLaunchroid.git
git fetch upstream

# 2. Start merge on the test branch
git checkout test
git merge upstream/main --no-ff

# 3. Resolve conflicts
#    - Keep fork's stubs for: StorageMigrationManager, MinecraftPlayStoreValidator,
#      PackageNameLockManager, MainActivity migration dialog
#    - For InbuiltOverlayManager.java: take upstream, then re-add memory overlay methods
#    - For strings.xml: keep both, then check for orphaned fragments

# 4. After resolving, verify memory editor still compiles
#    grep -rn 'addMemoryOverlay\|removeMemoryOverlay' app/src/main/java/

# 5. Push and let CI verify
git push origin test
```

### Files to Watch

- `InbuiltOverlayManager.java` — Upstream refactors here may drop `addMemoryOverlay`/`removeMemoryOverlay`
- `strings.xml` — Migration strings may get added back; fork keeps them as placeholders
- `build.gradle` — SQLCipher dependency may reappear
- `MainActivity.java` — Migration/Play Store dialogs may get re-added
- Any file in `memoryeditor/` — These are fork-only, ensure they still compile

## Appendix: Analysis Standards

All investigation, debugging, and maintenance work on this fork follows the [Operation Guide](operation-guide.md). The guide defines:

- **Scope boundaries** — what is in-bounds for analysis and what is not
- **Evidence priorities** — runtime behavior over static code, network traffic over comments
- **Standard procedures** — passive recon first, runtime tracing, narrow-path validation
- **Reproducibility requirements** — every finding must be independently verifiable from a clean baseline

Refer to the [full document](operation-guide.md) before conducting any structured analysis.
