@echo off
set PROJECT_PATH=%~dp0
set JDK_PATH=C:\Program Files\Android\Android Studio\jbr
set GRADLEW_PATH=%PROJECT_PATH%\gradlew.bat
set JAVA_HOME=%JDK_PATH%
set PATH=%JAVA_HOME%\bin;%PATH%
set APK_FILE=%PROJECT_PATH%\app\build\outputs\apk\debug\app-debug.apk
cd %PROJECT_PATH%
echo [1/5]Building project...
call %GRADLEW_PATH% assembleDebug > nul 2>&1
echo [2/5]Installing...
adb install -r %APK_FILE% > nul 2>&1
echo [3/5]Adding systemui to lasposed scope...
adb shell "su -c ./data/adb/lspd/bin/cli scope set ru.liner.sensorpermission -a com.android.systemui/0"
echo [4/5]Rebooting...
adb reboot now> nul 2>&1
echo [5/5]Done...
