# Redface — AI Workspace Memory

Timeline of research, decisions, and progress.

---

## 2026-03-30 — Initial audit

### Project overview
- Android client for forum.hardware.fr, published on Play Store
- Version 5.1.0, last commit Sept 2025
- Maintained under ForumHFR GitHub org
- License: Apache 2.0

### Tech stack
- Java 11 (no Kotlin), minSdk 16, targetSdk 35 (Android 15)
- Dagger 2 DI, RxJava 1.x, ButterKnife, Otto event bus
- MVP-style architecture with fragments
- HTML templates for post rendering (PostTemplate, SmileysTemplate)
- ~230 source files, 13 test files (minimal coverage)

### Dependencies health
| Library | Current | Latest | Risk |
|---------|---------|--------|------|
| Retrofit | 1.9.0 | 2.11+ | CRITICAL — API incompatible, last update 2016 |
| OkHttp | 3.14.9 | 4.12+ | HIGH — no TLS 1.3, EOL |
| RxJava | 1.3.8 | 3.x | HIGH — deprecated |
| Glide | 4.9.0 | 4.16+ | MEDIUM |
| Firebase BOM | 28.4.2 | 33+ | HIGH — 2020 era |
| ButterKnife | 10.2.0 | — | MEDIUM — deprecated, use ViewBinding |
| Otto | 1.3.5 | — | LOW — works, but 2014 lib |
| Dagger | 2.46.1 | 2.51+ | LOW |
| AndroidX/Material | up to date | — | OK |

### Architecture notes
- Single module (app only)
- DI: RedfaceApp → DaggerApplication → RedfaceComponent (singleton)
- Data layer: api/ (Retrofit), provider/, state/ (CategoriesStore, ResponseStore)
- UI: 13 activities, 12 fragments, custom adapters
- Network: custom cookie management, OkHttp interceptors
- Background: WorkManager for PM notifications
- Build variants: debug / beta / release (ProGuard on beta+release)
- CI/CD: GitHub Actions, auto-sign APK on release tag

### Branch situation
- `master` — production
- `develop` — active dev branch
- Several old feature branches (okhttp3-migration, butterknife-migration, etc.)
- Some migration work was started but apparently never merged

### Key observations
- minSdk 16 (Android 4.1) is extremely low, could be raised to 21+ safely
- Retrofit 1.x → 2.x is the biggest migration debt
- An okhttp3 migration branch exists (161-okhttp3-migration) — check if usable
- A butterknife migration branch exists (develop_butterknife_migration) — check state
- Test coverage is almost nonexistent
