package com.github.wujiayi.appium.screens.ios;

import com.github.wujiayi.appium.consts.CapabilityType;
import com.github.wujiayi.appium.consts.MobileCtrlException;
import com.github.wujiayi.appium.ctrl.AndroidCtrl;
import com.github.wujiayi.appium.ctrl.IOSCtrl;
import com.github.wujiayi.appium.ctrl.MobileCtrl;
import io.appium.java_client.remote.MobilePlatform;
import org.openqa.selenium.By;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Base screen class provides setup and tear down code.
 *
 * @author chris.wu
 */
public abstract class Screen {

    protected static MobileCtrl mobileCtrl;

    public void waitForScreen() {
        waitForScreen(CapabilityType.DEFAULT_WAIT_TIME);
    }

    public abstract void waitForScreen(int timeout);

    protected void waitForScreen(By[] atThis, int timeout) {
        try {
            for (By by : atThis) {
                mobileCtrl.findElement(by, timeout);
            }
        } catch (MobileCtrlException e) {
            throw new MobileCtrlException(String.format("%s, not at %s", e.getMessage(), getScreenName()));
        }
    }

    public void setUp(String platform, String appPath) {
        mobileCtrl = createMobileCtrl(platform);
        mobileCtrl.setUp(appPath);
    }

    private MobileCtrl createMobileCtrl(String platform) {
        if (platform.equals(MobilePlatform.ANDROID)) {
            return new AndroidCtrl();
        } else {
            return new IOSCtrl();
        }
    }

    protected String getScreenName() {
        String screenName = "";
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        if (ste.length > 3) {
            screenName = ste[3].getClassName();
            Matcher m = Pattern.compile("\\.([^\\.]+Screen)").matcher(screenName);
            if (m.find()) {
                screenName = m.group(1);
            }
            screenName = screenName.replaceAll("(?<=[a-z])(?=[A-Z])", " ");
        }
        return screenName;
    }

    public void reset() {
        mobileCtrl.reset();
    }

    public void tearDown() {
        mobileCtrl.tearDown();
    }

    public boolean verifyText(String text) {
        return verifyText(text, CapabilityType.DEFAULT_WAIT_TIME);
    }

    public boolean verifyText(String text, int timeout) {
        return mobileCtrl.verifyText(text, timeout);
    }

    public void takeScreenShot(String fileName, String dir) {
        mobileCtrl.takeScreenshot(fileName, dir);
    }

    public void startVideo() {
        mobileCtrl.startVideo();
    }

    public void endVideo(String dirPath, String filename) {
        mobileCtrl.endVideo(dirPath, filename);
    }

    public void startLog() {
        mobileCtrl.startLog();
    }

    public void endLog(String dirPath, String filename) {
        mobileCtrl.endLog(dirPath, filename);
    }

    public void acceptAlert() {
        acceptAlert(CapabilityType.DEFAULT_WAIT_TIME);
    }

    public void acceptAlert(int timeout) {
        mobileCtrl.acceptAlert(timeout);
    }

    public void dismissAlert() {
        dismissAlert(CapabilityType.DEFAULT_WAIT_TIME);
    }

    public void dismissAlert(int timeout) {
        mobileCtrl.dismissAlert(timeout);
    }


}
