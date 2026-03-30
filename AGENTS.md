# Redface — AI Agent Instructions

## Project
Android client for forum.hardware.fr (HFR). Java 11, Gradle, single-module.

## Identity
- Git user: xat / xat@azora.fr
- GitHub account: XaaT (member of ForumHFR org)
- Use local git config only, never --global

## Memory
Read `MEMORY.md` at the start of each conversation to load timeline context.

## Conventions
- Language: code and commits in English, communication in French
- Commit style: follow existing repo convention (lowercase, imperative or descriptive)
- Branch naming: `xat/<feature>` for our branches
- Never force-push to master or develop
- PR target: `develop` (not master) unless hotfix

## Architecture
- Dagger 2 DI, MVP-style fragments, RxJava 1.x
- HTML templates for post rendering
- Build variants: debug / beta / release
- CI: GitHub Actions (.github/workflows/android.yml)

## Known debt
- Retrofit 1.9.0 (critical), OkHttp 3.14.9, RxJava 1.x, ButterKnife, Firebase BOM 28
- See MEMORY.md for full dependency audit
