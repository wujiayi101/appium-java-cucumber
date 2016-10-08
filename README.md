# Appium Test Example
This project is an unified iOS+Android test project that is built on top of [appium-wrapper](https://github.com/wujiayi47/appium-wrapper), and make sure of Page Object Pattern and Cucumber JVM tools to improve Maintainability and traceability of test scripts.  


## Test Environment Setup

### Hardware :
* Mac
* An Android Device (enabled debug mode) connected to Mac
* No iOS physical device is needed at this moment, because mobile automation is now running on Simulator

### Install Appium Server
#### Command Line Version
Install Homebrew: 
```
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```
Install node.js and npm :
```
brew install node
```
Install appium by npm
```
npm install -g appium@1.4.13
```


#### GUI Version
Install version 1.4.13 
```
https://bitbucket.org/appium/appium.app/downloads/
```

Install JAVA JDK and Maven

Check if java jdk (1.8 or higher) is already install on your mac:
```
java -version
```
You should see following output if java already installed
```
java version "1.8.0_65
Java(TM) SE Runtime Environment (build 1.8.0_65-b17)
Java HotSpot(TM) 64-Bit Server VM (build 25.65-b01, mixed mode)
```
Download and install JAVA SDK if it is not yet installed (choose Mac OS X version) 
```
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
```
Install Maven
```
brew install maven
```
Download and put a pre-configured maven settings.xml file to ~/.m2

### Android & iOS Software Dependencies
#### Android
1. Download Android Studio 2.0: http://developer.android.com/sdk/index.html
Launch Android Studio - Tools - SDK Manager, update and download latest SDK tools
2. Add JAVA JDK and Android SDK environment variable to ~/.bash_profile:
```
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home 
export ANDROID_HOME=~/Library/Android/sdk
export PATH=$PATH:$JAVA_HOME:$JAVA_HOME/bin:$ANDROID_HOME:$ANDROID_HOME/platform-tools:$ANDROID_HOME/platform-tools/adb
```

3. Save .bash_profile and run:
```
source ~/.bash_profile
```

#### iOS
1. Download xCode7.3 stable release version: https://developer.apple.com/xcode/download/
2. Under xcode, Add simulator "iPhone 6 + OS version 9.3"
3. Verify iOS & Android dependencies are installed, to verify that iOS and Android dependencies are met you can use appium-doctor.
```
Install appium-doctor
npm install -g appium-doctor
```
Check installation completeness
```
./appium-doctor
```
If everything install successfully, you should see output like this:
```
Running Android Checks
✔ ANDROID_HOME is set to "/Users/chris_wu/Library/Android/sdk"
✔ JAVA_HOME is set to "/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home."
✔ ADB exists at /Users/chris_wu/Library/Android/sdk/platform-tools/adb
✔ Android exists at /Users/chris_wu/Library/Android/sdk/tools/android
✔ Emulator exists at /Users/chris_wu/Library/Android/sdk/tools/emulator
✔ Android Checks were successful.

✔ All Checks were successful
Chris-Wu-MacBook-Pro:ui-test-walking chris_wu$ appium-doctor --ios
Running iOS Checks
✔ Xcode is installed at /Applications/Xcode.app/Contents/Developer
✔ Xcode Command Line Tools are installed.
✔ DevToolsSecurity is enabled.
✔ The Authorization DB is set up properly.
✔ Node binary found at /usr/local/bin/node
✔ iOS Checks were successful.

✔ All Checks were successful
```
### iOS Instrument Patch:
Apple's Instruments / UIAutomation tool have an built in delay issue, so the automation test runs very slow. There is a third party patch fixed the issue. We created an script to apply the patch without any pains.
```
git clone https://github.com/wujiayi47/ios-sim-appcelerator.git
cd ios-sim-appcelerator
./accelerate_ios_sim.sh
```

## Run Automation Test

### Start appium server 
```
./start_android_server.sh
```
or
```
./start_ios_server.sh
```
### Run appium test
`CukeTest.java` will be the entry point of your project, run CukeTest with following VM arguments:
```
-Dplatform=iOS -Dapp="xxx.app" -Dcucumber.options="--tags @login" -Dserver=UAT
```
or
```
-Dplatform=Android -Dapp="xxx.apk" -Dcucumber.options="--tags @login" -Dserver=UAT
```
