# 🛠️ Setup Guide

## Prerequisites Check

Run these commands to verify your environment:

```bash
# Java 11+ (required for Kotlin/Gradle)
java -version
# Output should show: openjdk/javac 11+

# Git
git --version
# Output should show: git version 2.x+

# Node 16+ (for React web app)
node --version
# Output should show: v16+

npm --version
# Output should show: 8+
```

If any are missing, install them:

### macOS
```bash
# Install Java
brew install openjdk@11

# Install Node
brew install node
```

### Linux (Ubuntu/Debian)
```bash
# Install Java
sudo apt update
sudo apt install openjdk-11-jdk

# Install Node
sudo apt install nodejs npm
```

### Windows
- **Java:** Download from [oracle.com](https://www.oracle.com/java/technologies/downloads/) or [openjdk.java.net](https://openjdk.java.net/)
- **Node:** Download from [nodejs.org](https://nodejs.org/)

---

## Step 1: Clone & Initialize Project

```bash
# Create fresh folder
mkdir -p ~/projects
cd ~/projects

# Clone the repo (after you create it on GitHub)
git clone https://github.com/nidente/VideoPokerKMP.git
cd VideoPokerKMP

# Verify structure
ls -la
# Should show: README.md, .gitignore, docs/, etc.
```

---

## Step 2: Copy Android Source Code

Copy your existing game logic from `/home/landoulsi/L3/kotlin/l3-ihm-tp4-nidente/app/src/main/java/com/example/tp4/`:

```bash
# From your current Android project, copy these files:
cp ~/L3/kotlin/l3-ihm-tp4-nidente/app/src/main/java/com/example/tp4/Card.kt \
   ~/L3/kotlin/l3-ihm-tp4-nidente/app/src/main/java/com/example/tp4/Deck.kt \
   ~/L3/kotlin/l3-ihm-tp4-nidente/app/src/main/java/com/example/tp4/Hand.kt \
   ~/L3/kotlin/l3-ihm-tp4-nidente/app/src/main/java/com/example/tp4/Poker.kt \
   ~/L3/kotlin/l3-ihm-tp4-nidente/app/src/main/java/com/example/tp4/Session.kt \
   /tmp/VideoPokerKMP/source-files/

# These will be extracted to commonMain during Week 1
```

---

## Step 3: Create KMP Project Skeleton (Done in Week 1)

In **Week 1 Day 1**, we'll:

1. Generate KMP project with Jetbrains CLI:
   ```bash
   cd ~/projects/VideoPokerKMP
   
   # Create shared KMP library
   mkdir -p shared/src/commonMain/kotlin/com/example/videopoker
   mkdir -p shared/src/androidMain/kotlin/com/example/videopoker
   mkdir -p shared/src/jsMain/kotlin/com/example/videopoker
   
   # Create Android app folder
   mkdir -p android
   
   # Create React web folder
   mkdir -p web
   ```

2. Add `build.gradle.kts` files (templates in Week 1 docs)

3. Set up GitHub Pages hosting for React

---

## Step 4: Verify Build Environment

Before Week 1 starts:

```bash
cd ~/projects/VideoPokerKMP

# Verify Gradle wrapper exists
ls -la gradle/wrapper/

# Verify Android SDK installed
$ANDROID_HOME/tools/sdkmanager --list
# (if error, install Android SDK from Android Studio)

# Verify Node modules available
npm --version
```

---

## Common Issues

### Issue: `java: command not found`
**Solution:** 
```bash
# Set JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
# Add to ~/.bash_profile or ~/.zshrc for persistence
```

### Issue: `gradlew: permission denied`
**Solution:**
```bash
chmod +x gradlew
./gradlew --version
```

### Issue: `Android SDK not found`
**Solution:**
```bash
# Install from Android Studio
# Or set path:
export ANDROID_HOME=~/Library/Android/sdk
export PATH=$ANDROID_HOME/tools:$PATH
```

### Issue: npm install fails
**Solution:**
```bash
# Clear cache and retry
npm cache clean --force
npm install
```

---

## Environment Setup for Week 1

Set these environment variables before starting:

```bash
# ~/.bash_profile or ~/.zshrc (add these lines)

# Java
export JAVA_HOME=$(/usr/libexec/java_home -v 11)

# Android
export ANDROID_HOME=~/Library/Android/sdk
export PATH=$ANDROID_HOME/tools:$PATH

# Kotlin
export KOTLIN_HOME=/usr/local/opt/kotlin
export PATH=$KOTLIN_HOME/bin:$PATH
```

Then reload:
```bash
source ~/.bash_profile  # or ~/.zshrc
```

---

## IDE Setup (Optional but Recommended)

### Android Studio
- Install from [developer.android.com](https://developer.android.com/studio)
- Open `VideoPokerKMP` folder
- Wait for Gradle sync
- Android SDK will auto-install

### VS Code (for React web)
- Install from [code.visualstudio.com](https://code.visualstudio.com)
- Extensions:
  - ES7+ React/Redux/React-Native snippets
  - TypeScript Vue Plugin
  - Prettier

### IntelliJ IDEA (Full project)
- Install Community Edition (free)
- Open `VideoPokerKMP` folder
- Kotlin plugin auto-enables

---

## Ready for Week 1?

Checklist:
- [ ] Java 11+ installed and verified
- [ ] Git installed
- [ ] Node 16+ installed
- [ ] GitHub account ready
- [ ] Read 01-README.md
- [ ] Read 02-ARCHITECTURE.md
- [ ] This file (03-SETUP_GUIDE.md)

**Next:** Create repo on GitHub and read **04-WEEK1_TASKS.md** to start!

---

**Last updated:** Aug 15, 2026
