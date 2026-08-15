# 🚀 Deployment Guide

## GitHub Pages Setup (Web)

### Prerequisites
- GitHub account
- GitHub repo created (public or private with Pages enabled)
- `gh-pages` package installed

### One-Time Setup

```bash
cd ~/projects/VideoPokerKMP/web

# Install deployment package
npm install --save-dev gh-pages

# Verify vite.config configured with base URL
# base: "/VideoPokerKMP/"
```

### Deploy Process

```bash
cd ~/projects/VideoPokerKMP/web

# Build React
npm run build

# Deploy to GitHub Pages
npm run deploy

# Verify: Open https://nidente.github.io/VideoPokerKMP/
```

### Manual Deployment (Alternative)

```bash
# Build
npm run build

# Push dist/ folder to gh-pages branch
git add dist/
git commit -m "build: Deploy web app"
git push origin master

# GitHub Actions auto-deploys from /docs or gh-pages branch
```

---

## Android Deployment (APK Build)

### Prerequisites
- Android SDK installed
- Android Keystore created (for signing)
- Gradle wrapper in project

### Build Debug APK

```bash
cd ~/projects/VideoPokerKMP/android

# Build debug APK
./gradlew assembleDebug

# Output: android/build/outputs/apk/debug/app-debug.apk
# Install on device/emulator
adb install build/outputs/apk/debug/app-debug.apk
```

### Build Release APK (For App Store)

```bash
# Create keystore (one-time)
keytool -genkey -v -keystore release.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias videopoker

# Build signed release APK
./gradlew assembleRelease

# Output: android/build/outputs/apk/release/app-release.apk
```

### Deploy to Google Play

1. Create Google Play Developer account ($25 one-time)
2. Create app entry in Play Console
3. Upload signed APK
4. Wait for review (~2-4 hours)
5. Release to production

---

## iOS Deployment (Phase 2)

### Prerequisites
- macOS with Xcode installed
- Apple Developer account ($99/year)
- iOS device or simulator

### Build & Run

```bash
cd ~/projects/VideoPokerKMP/iosApp

# Open Xcode
open VideoPokerApp.xcodeproj

# Or build from command line
xcodebuild -scheme VideoPokerApp -configuration Release

# Output: iosApp/build/Release-iphoneos/VideoPokerApp.ipa
```

### Deploy to App Store

1. Create App Store Connect entry
2. Build and archive in Xcode
3. Upload to TestFlight for review
4. Release to App Store

---

## Troubleshooting

### GitHub Pages Not Updating

```bash
# Clear gh-pages cache
npm run deploy -- --rm
npm run deploy
```

### Android Build Fails

```bash
# Clean and rebuild
./gradlew clean build

# Verify Java version
java -version  # Should be 11+
```

### iOS Build Issues

```bash
# Update CocoaPods
sudo gem install cocoapods
pod repo update
pod install

# Rebuild
cd iosApp
xcodebuild clean build
```

---

**Last updated:** Aug 15, 2026
