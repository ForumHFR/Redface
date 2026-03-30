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

### Branch audit
- `develop` is ahead of master: bumps SDK 35→36, removes edge-to-edge workaround, 3 files changed
- `161-okhttp3-migration` — 2016, dead
- `develop_butterknife_migration` — 2019, dead
- `dependencies-cleanup` — 2017, WIP abandoned
- `caching-layer-enhancements` — 2017, dead
- None of the old migration branches are recoverable

### Open PRs (all stale)
- #217 Fix super-h rehost permission denial (Oct 2021)
- #216 Fix super-h vs proguard (Oct 2021)
- #211 Super-H rehosting option (Jan 2021)
- #209 Correction bug messages disparaissent (Sept 2019)

### Open issues — 49 total
Recent (2025-2026):
- #240 Couleur barre de navigation (enhancement)
- #239 Ouvrir image = 403 (bug)
- #238 Appui long sur titre topic
- #237 Telechargement images crash
- #236 Scroll pages saute
- #235 Voir messages de citations
- #228 Fenetre redaction disparait
- #227 Texte qui disparait (bug)
- #225 Inutilisable sous HyperOS 2

Old (2016-2019): ~40 issues, mostly untreated bugs and enhancements from 2016 era. Major cleanup needed.

### develop vs master audit
Branches have **diverged**:

master has 3 commits not in develop (cherry-picks, never merged back):
- e59f781 Bump versionCode 5100→5101 (Play Console requirement)
- 16bb085 Revert to API 35 + opt-out edge-to-edge
- 23a60d0 Fix dollar signs + PM profiles

develop has 14 commits not in master:
- SDK 36 bump (reverted on master)
- WorkManager crash fix for Android 12
- PendingIntent FLAG_IMMUTABLE fix
- WebView response headers fix (pre-N)
- Changelog updates

Key diffs: master=SDK35/versionCode5101/edge-to-edge-optout, develop=SDK36/versionCode5100/no-edge-to-edge-workaround

**Decision**: to work from develop, must first reconcile the 3 master-only commits. The SDK 35 revert on master suggests SDK 36 caused issues — investigate before rebasing.
