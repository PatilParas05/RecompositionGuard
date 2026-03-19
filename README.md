# 🔍 RecompositionGuard

[![](https://jitpack.io/v/PatilParas05/RecompositionGuard.svg)](https://jitpack.io/#PatilParas05/RecompositionGuard)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg)](https://android-arsenal.com/api?level=26)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6%2B-green.svg)](https://developer.android.com/jetpack/compose)

A lightweight **Jetpack Compose** debug library that automatically detects, visualizes, and logs unnecessary recompositions in real time — without needing to open Android Studio's Layout Inspector.

> ⚠️ Intended for **debug builds only**.

---

## 🎯 The Problem

Compose performance is silently killed by unnecessary recompositions. Detecting them manually is tedious and requires profiling tools. **RecompositionGuard makes it automatic and always-on during development.**

---

## ✨ Features

- 📊 **Live floating overlay** — shows recomposition count per composable, updated every 100ms
- 🎨 **Color-coded severity** — 🟢 OK / 🟡 Moderate / 🔴 Excessive
- 📝 **Logcat suggestions** — tells you exactly why a composable is recomposing and how to fix it
- ⚙️ **Configurable thresholds** — set your own warn/error limits
- 🪶 **Zero-overhead design** — raw counts stored in plain `HashMap`, display state flushed via coroutine every 100ms (no recomposition cascade)
- 🎯 **Two tracking APIs** — `Modifier.trackRecomposition()` or `TrackRecomposition()` composable

---

## 📸 Preview

```
🔍 RecompositionGuard
───────────────────────────────
RapidUnstableComposable [10x] 🔴
RapidHotComposable      [10x] 🔴
RapidColdComposable      [1x] 🟢
```

Logcat:
```
[🔴 EXCESSIVE] Composable: "RapidHotComposable" recomposed 10 time(s)
⚠️  [RapidHotComposable] recomposed 10 times. Possible causes:
   -> Unstable lambda - wrap with remember { }
   -> Data class missing @Stable or @Immutable annotation
   -> State read inside composition - hoist it up
   -> Inline function triggering parent recomposition
   -> Use derivedStateOf { } for computed state
```

---

## 🚀 Installation

### Step 1 — Add JitPack to your root `settings.gradle`

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2 — Add the dependency

```groovy
dependencies {
    debugImplementation 'com.github.PatilParas05:RecompositionGuard:1.0.0'
}
```

Or in `build.gradle.kts`:
```kotlin
dependencies {
    debugImplementation("com.github.PatilParas05:RecompositionGuard:1.0.0")
}
```

> 💡 Use `debugImplementation` so the library is **never included in release builds**.

---

## 🛠️ Usage

### Step 1 — Install in `MainActivity.onCreate()`

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Install ONCE here — NOT inside setContent
        RecompositionGuard.install(
            ThresholdConfig(
                warnThreshold  = 3,    // 🟡 yellow after 3 recompositions
                errorThreshold = 8,    // 🔴 red after 8 recompositions
                overlayEnabled = true,
                logsEnabled    = true,
                dashboardEnabled = true
            )
        )

        setContent {
            YourAppTheme {
                YourRootScreen()
            }
        }
    }
}
```

### Step 2 — Add the dashboard overlay to your root composable

```kotlin
@Composable
fun YourRootScreen() {
    Box {
        YourContent()
        RecompositionDashboard() // floating overlay, top-right by default
    }
}
```

### Step 3 — Track your composables

**Option A — Modifier (recommended, attach to any composable):**
```kotlin
@Composable
fun ProductCard(product: Product) {
    Text(
        text = product.name,
        modifier = Modifier.trackRecomposition("ProductCard")
    )
}
```

**Option B — Composable function (use inside composable body):**
```kotlin
@Composable
fun HomeScreen() {
    TrackRecomposition("HomeScreen")
    // ... rest of your UI
}
```

---

## 📋 Full Example

```kotlin
class RapidViewModel : ViewModel() {
    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter

    fun increment() { _counter.value++ }
}

@Composable
fun RapidTestScreen(vm: RapidViewModel = viewModel()) {
    val counter by vm.counter.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            vm.increment()
        }
    }

    Box(Modifier.padding(16.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Counter: $counter")

            // ✅ Recomposes every tick — will show increasing count
            HotComposable(count = counter)

            // ✅ Never changes — stays at [1x]
            ColdComposable()

            // ✅ Unstable String parameter — recomposes every tick
            UnstableComposable(value = counter.toString())
        }

        RecompositionDashboard(alignment = Alignment.Center)
    }
}

@Composable
fun HotComposable(count: Int) {
    Text(
        text = "🔥 Hot: $count",
        modifier = Modifier.trackRecomposition("HotComposable")
    )
}

@Composable
fun ColdComposable() {
    Text(
        text = "❄️ Cold: Static Content",
        modifier = Modifier.trackRecomposition("ColdComposable")
    )
}

@Composable
fun UnstableComposable(value: String) {
    Text(
        text = "⚠️ Unstable: $value",
        modifier = Modifier.trackRecomposition("UnstableComposable")
    )
}
```

---

## ⚙️ Configuration

```kotlin
ThresholdConfig(
    warnThreshold    = 5,     // recompositions before 🟡 warning (default: 5)
    errorThreshold   = 10,    // recompositions before 🔴 error (default: 10)
    overlayEnabled   = true,  // show/hide the colored border on tracked composables
    logsEnabled      = true,  // enable/disable logcat output
    dashboardEnabled = true   // show/hide the floating dashboard panel
)
```

**Dashboard position:**
```kotlin
// Top-right (default)
RecompositionDashboard()

// Center
RecompositionDashboard(alignment = Alignment.Center)

// Bottom-start
RecompositionDashboard(alignment = Alignment.BottomStart)

// Custom flush interval (default 100ms)
RecompositionDashboard(flushIntervalMs = 500L)
```

---

## 🧠 How It Works

```
SideEffect fires (from trackRecomposition modifier or TrackRecomposition())
  → track() increments plain HashMap (rawCounts)
  → NO Compose state written → NO recomposition cascade ✅

Every 100ms (coroutine in RecompositionDashboard)
  → flush() copies rawCounts → SnapshotStateMap (data)
  → Overlay recomposes once with updated counts ✅
```

The **two-map pattern** is the core innovation — tracking itself never triggers extra recompositions.

---

## 📦 API Reference

| API | Description |
|---|---|
| `RecompositionGuard.install(config)` | Initialize — call once in `onCreate` before `setContent` |
| `RecompositionGuard.reset()` | Reset all tracked counts |
| `ThresholdConfig` | Configure warn/error thresholds and feature flags |
| `Modifier.trackRecomposition("name")` | Track via modifier — attach to any composable |
| `TrackRecomposition("name")` | Track via composable function — use inside body |
| `RecompositionDashboard()` | Floating overlay showing live counts |
| `RecompositionTracker.track("name")` | Low-level tracking — use inside `SideEffect { }` |
| `RecompositionTracker.data` | `SnapshotStateMap` of all tracked composables |
| `RecompositionTracker.getCount("name")` | Get current count for a specific composable |

---

## 🚫 Common Mistake

The tracker must be in the **same recomposition scope** as the state being read:

```kotlin
// ✅ CORRECT — trackRecomposition is in the same scope as `count`
@Composable
fun HotComposable(count: Int) {
    Text(
        text = "Hot: $count",
        modifier = Modifier.trackRecomposition("HotComposable")
    )
}

// ❌ WRONG — SideEffect in a wrapper scope never fires when content recomposes
@Composable
fun GuardedComposable(name: String, content: @Composable () -> Unit) {
    SideEffect { RecompositionTracker.track(name) } // This won't work reliably
    Box { content() }
}
```

---

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first.

---

## 📄 License

```
MIT License — Copyright (c) 2026 Paras Patil
```

---

<p align="center">Made with ❤️ by <a href="https://github.com/PatilParas05">Paras Patil</a></p>
