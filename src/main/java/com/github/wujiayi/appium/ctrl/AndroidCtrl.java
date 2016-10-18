package com.github.wujiayi.appium.ctrl;

import com.github.wujiayi.appium.consts.AppiumServer;
import com.github.wujiayi.appium.consts.CapabilityType;
import com.github.wujiayi.appium.consts.MobileCtrlException;
import com.github.wujiayi.appium.ctrl.driver.AndroidTouchDriver;

import io.appium.java_client.remote.AutomationName;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.DesiredCapabilities;


/**
 * @author chris.wu
 */
public class AndroidCtrl extends MobileCtrl {

    Process capVideoProc = null;
    Process loggingProc = null;
    String LOG_TEMP_FILE = "/sdcard/aos_tmp_log";
    String VIDEO_TEMP_FILE = "/sdcard/videocap.mp4";

    @Override
    public void clearText(By by, int timeout) {
        WebElement webElement = findElement(by, timeout);

        logger.debug(String.format("clearText %s", by.toString()));
        clearText(webElement, timeout);
    }

    @Override
    public void clearText(WebElement elem, int timeout) {
        try {
            elem.clear();
        } catch (RuntimeException e) {
            throw new MobileCtrlException("Cannot clear text");
        }
    }

    @Override
    public void clickTextField(By by, int timeout) {
        click(by, timeout);
    }

    /**
     * @param by      identifier of the text field
     * @param text    string value to be entered
     * @param timeout try until [timeout] seconds
     */
    @Override
    public void enterText(By by, String text, int timeout, boolean type) {
        WebElement elem = findElement(by, timeout);

        if (null == by) {
            logger.debug(String.format("enterText \"%s\"", text));
        } else {
            logger.debug(String.format("enterText %s \"%s\"", by.toString(), text));
        }

        enterText(elem, text, timeout, type);
    }

    @Override
    public void enterText(By parentBy, By childBy, String text, int timeout,
                          boolean type) {
        WebElement elem = findElement(parentBy, childBy, timeout);

        if ((null == parentBy) || (null == childBy)) {
            logger.debug(String.format("enterText \"%s\"", text));
        } else {
            logger.debug(String.format("enterText %s, %s \"%s\"",
                    parentBy.toString(), childBy.toString(), text));
        }

        enterText(elem, text, timeout, type);
    }

    @Override
    public boolean enterText(WebElement elem, String text, int timeout,
                             boolean type) {
        WebElement subElement;
        try {
            subElement = elem.findElement(By.id("edit_text"));
        } catch (RuntimeException e) {
            try {
                elem.sendKeys(text);
            } catch (RuntimeException e2) {
                throw new MobileCtrlException(String.format(
                        "cannot enter text after %d ms", timeout));
            }
            return true;
        }

        try {
            subElement.sendKeys(text);
        } catch (RuntimeException e) {
            throw new MobileCtrlException(String.format(
                    "cannot enter text after %d ms", timeout));
        }
        return true;
    }

    /**
     * @param appPath full path of the .apk
     */
    @Override
    public void setUp(String appPath) {
        URL remoteAddress = null;
        try {
            remoteAddress = new URL(AppiumServer.ANDROID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(CapabilityType.DEVICE_NAME, "Android Phone");
        desiredCapabilities.setCapability(CapabilityType.APP, appPath);
        desiredCapabilities.setCapability(CapabilityType.AUTOMATION_NAME, AutomationName.SELENDROID);
        desiredCapabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, Boolean.TRUE);
        desiredCapabilities.setCapability(CapabilityType.UNICODE_KEYBOARD, Boolean.TRUE);
        desiredCapabilities.setCapability(CapabilityType.RESET_KEYBOARD, Boolean.TRUE);
        desiredCapabilities.setCapability(CapabilityType.NEW_COMMAND_TIMEOUT, 70000);

        driver = new AndroidTouchDriver(remoteAddress, desiredCapabilities);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    @Override
    public void click(int x, int y) {
        runBashCommand("adb shell input click " + x + " " + y);
    }

    @Override
    public String getText(By by, int timeout) {
        WebElement webElement = findElement(by, timeout);

        logger.debug(String.format("getText %s", by.toString()));
        return getText(webElement);
    }

    @Override
    public String getText(By parentBy, By childBy, int timeout) {
        WebElement webElement = findElement(parentBy, childBy, timeout);

        logger.debug(String.format("getText %s, %s", parentBy.toString(),
                childBy.toString()));
        return getText(webElement);
    }

    @Override
    public String getText(WebElement elem) {
        return elem.getText();
    }

    @Override
    public WebElement findElement(WebElement elem, By by, int timeout) {
        return findElement(elem, by, true, timeout);
    }

    @Override
    public WebElement findElement(By parentBy, By childBy, int timeout) {
        WebElement elem = findElement(null, parentBy, true, timeout);
        return findElement(elem, childBy, true, timeout);
    }

    @Override
    public List<WebElement> findElements(By by, int timeout) {
        return findElements(by, true, timeout);
    }

    @Override
    public void sendKeyEvent(CharSequence... keyToSend) {
        new Actions(driver).sendKeys(keyToSend).perform();
    }

    @Override
    public boolean verifyText(String text, int timeout) {
        long start = System.currentTimeMillis();
        do {
            try {
                driver.findElement(By.partialLinkText(text));

                // if it is a confirm dialog, click button to dismiss
                acceptAlert(1000);
                return true;

            } catch (NoSuchElementException ignored) {
            }
        } while (System.currentTimeMillis() - start < timeout);
        return false;
    }

    @Override
    public void startVideo() {
        capVideoProc = runBashCommand("adb shell screenrecord --bit-rate 8000000 " + VIDEO_TEMP_FILE);
    }

    @Override
    public void endVideo(String destDir, String fileName) {
        endProcessAndMoveFile(capVideoProc, VIDEO_TEMP_FILE, destDir, fileName);
    }

    @Override
    public void startLog() {
        // clear log
        runBashCommand("adb logcat -c");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // clear previous log
        try {
            runBashCommand("adb shell rm " + LOG_TEMP_FILE).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // start logging and redirect log to temp file
        loggingProc = runBashCommand("adb logcat -v time -f " + LOG_TEMP_FILE);
    }

    @Override
    public void endLog(String destDir, String filename) {
        endProcessAndMoveFile(loggingProc, LOG_TEMP_FILE, destDir, filename);
    }

    private void endProcessAndMoveFile(Process process, String tempFilePath, String destDir, String filename) {
        if (process == null) {
            return;
        }
        try {
            // end process
            process.destroy();
            process.waitFor();

            Thread.sleep(2000);

            if (destDir != null) {
                // make sure destination folder created, ignore if already exist
                FileUtils.forceMkdir(new File(destDir));

                // pull file onto destination folder
                Process pullFileProcess = null;
                try {
                    pullFileProcess = runBashCommand("adb pull " + tempFilePath + " " + destDir + File.separator + filename);
                    pullFileProcess.waitFor();

                } catch (Exception e) {
                    e.printStackTrace();
                    if (null != process) {
                        process.destroy();
                    }
                    if (null != pullFileProcess) {
                        pullFileProcess.destroy();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != process) {
                process.destroy();
            }
        }
    }

    @Override
    public void acceptAlert(int timeout) {
        long start = System.currentTimeMillis();
        do {
            try {
                driver.findElement(By.id("button1")).click();
                break;
            } catch (Exception ignored) {
            }
        } while (System.currentTimeMillis() - start < timeout);
    }

    @Override
    public void dismissAlert(int timeout) {
        long start = System.currentTimeMillis();
        do {
            try {
                driver.findElement(By.id("button2")).click();
                break;
            } catch (Exception ignored) {
            }
        } while (System.currentTimeMillis() - start < timeout);
    }

    public void scrollDown(By elemBy, int movePixel) {
        WebElement elem = driver.findElement(elemBy);
        TouchActions flick = new TouchActions(driver).flick(elem, 0, -movePixel, 2);
        flick.perform();
    }

    public void tapBackSoftButton() {
        driver.navigate().back();
    }
}

