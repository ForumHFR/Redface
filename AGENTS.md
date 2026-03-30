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

## GitHub interactions
- Every issue, comment, or PR created by an AI agent **must** state who requested it
- Format: "Commentaire/Issue/PR généré(e) par Claude (Opus 4.6) à la demande de @XaaT" (adapt the username to whoever is driving the session)
- This applies to all ForumHFR repos, not just Redface

## Architecture
- Dagger 2 DI, MVP-style fragments, RxJava 1.x
- HTML templates for post rendering
- Build variants: debug / beta / release
- CI: GitHub Actions (.github/workflows/android.yml)

## Known debt
- Retrofit 1.9.0 (critical), OkHttp 3.14.9, RxJava 1.x, ButterKnife, Firebase BOM 28
- See MEMORY.md for full dependency audit
