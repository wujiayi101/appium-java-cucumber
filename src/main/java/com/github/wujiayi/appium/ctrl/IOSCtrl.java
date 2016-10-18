package com.github.wujiayi.appium.ctrl;

import com.github.wujiayi.appium.consts.AppiumServer;
import com.github.wujiayi.appium.consts.CapabilityType;
import com.github.wujiayi.appium.consts.IosDevice;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.github.wujiayi.appium.consts.MobileCtrlException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chris.wu
 */
public class IOSCtrl extends Ctrl {

    String TEMP_LOG_FILE = "/tmp/appium.ios.log";

    @Override
    public void clearText(By by, int timeout) {
        throw new UnsupportedCommandException();
    }

    @Override
    public void clearText(WebElement elem, int timeout) {
        throw new UnsupportedCommandException();
    }

    @Override
    public void clickTextField(By by, int timeout) {
        List<WebElement> elems = findElements(by, timeout);

        logger.debug(String.format("clickTextField %s", by.toString()));
        for (WebElement elem : elems) {
            if (elem.getTagName().equals("UIATextField")) {
                try {
                    elem.click();
                } catch (RuntimeException e) {
                    throw new MobileCtrlException(String.format(
                            "element \"%s\" can not be clicked after %d ms",
                            by.toString(), timeout));
                }
                break;
            }
        }
    }

    @Override
    public void enterText(By by, String text, int timeout, boolean type) {
        if (null == by)
            enterText((WebElement) null, text, timeout, true);
        else {
            List<WebElement> webElements = findElements(by, timeout);
            for (WebElement w : webElements) {
                if (enterText(w, text, timeout, type))
                    return;
            }
        }
    }

    @Override
    public void enterText(By parentBy, By childBy, String text, int timeout,
                          boolean type) {
        enterText(findElement(parentBy, childBy, timeout), text, timeout, type);
    }

    @Override
    public boolean enterText(WebElement elem, String text, int timeout,
                             boolean type) {
        if (type) {
            String tagName = null == elem ? null : elem.getTagName();
            if (null == tagName || "UIATextField".equals(tagName)
                    || "UIASecureTextField".equals(tagName)
                    || "UIAElement".equals(tagName)
                    || "UIATextView".equals(tagName)
                    || "UIASearchBar".equals(tagName)
                    || "UIAButton".equals(tagName)) {

                if (null != elem) {
                    elem.click();
                }

                WebElement keyboard = findElement(By.className("UIAKeyboard"),
                        timeout);
                text = text.toLowerCase();
                logger.debug(String.format("typeText \"%s\"", text));

                for (int i = 0; i < text.length(); ++i) {
                    char c = text.charAt(i);
                    if (' ' == c) {
                        keyboard.findElement(By.id("space")).click();
                    } else if ('\n' == c) {
                        keyboard.findElement(By.id("return")).click();
                    } else {
                        keyboard.findElement(By.id("" + text.charAt(i)))
                                .click();
                    }
                }

                return true;
            } else
                return false;

        } else {
            logger.debug(String.format("setText \"%s\"", text));

            String tagName = elem.getTagName();
            if ("UIATextField".equals(tagName)
                    || "UIASecureTextField".equals(tagName)
                    || "UIAElement".equals(tagName)
                    || "UIATextView".equals(tagName)
                    || "UIASearchBar".equals(tagName)
                    || "UIAButton".equals(tagName)) {


                IOSElement m = (IOSElement) elem;
                m.setValue(text);
                return true;
            } else
                return false;
        }
    }


    /**
     * @param appPath full path of the .apk (Android)
     */
    @Override
    public void setUp(String appPath) {
        String deviceName = System.getProperty("device", IosDevice.iPhone_6);
        String osVersion = System.getProperty("os", "9.3");

        URL remoteAddress = null;
        try {
            remoteAddress = new URL(AppiumServer.IOS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        desiredCapabilities.setCapability(CapabilityType.DEVICE_NAME, deviceName);
        desiredCapabilities.setCapability(CapabilityType.PLATFORM_VERSION, osVersion);
        desiredCapabilities.setCapability(CapabilityType.APP, appPath);
        desiredCapabilities.setCapability("locationServicesEnabled", true);

        driver = new IOSDriver(remoteAddress, desiredCapabilities);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    @Override
    public String getText(By by, int timeout) {
        List<WebElement> webElements = findElements(by, timeout);

        logger.debug(String.format("getText %s", by.toString()));
        for (WebElement w : webElements) {
            String text = getText(w);
            if (null != text)
                return text;
        }
        return null;
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
        String tagName = elem.getTagName();
        if ("UIATextField".equals(tagName)
                || "UIASecureTextField".equals(tagName)
                || "UIAElement".equals(tagName)
                || "UIAStaticText".equals(tagName)
                || "UIAButton".equals(tagName)) {
            // if contains non-breaking space(ascii 160),
            // convert to regular space(ascii 32)
            return elem.getText().replaceAll("\u00A0", " ");
        }
        return null;
    }

    @Override
    public WebElement findElement(WebElement elem, By by, int timeout) {
        return findElement(elem, by, false, timeout);
    }

    @Override
    public WebElement findElement(By parentBy, By childBy, int timeout) {
        WebElement elem = findElement(null, parentBy, false, timeout);
        return findElement(elem, childBy, false, timeout);
    }

    @Override
    public List<WebElement> findElements(By by, int timeout) {
        return findElements(by, false, timeout);
    }

    @Override
    public void sendKeyEvent(CharSequence... keyToSend) {
        throw new UnsupportedCommandException("sendKeyEvent() not supported in iOS");
    }

    @Override
    public boolean verifyText(String text, int timeout) {

        long start = System.currentTimeMillis();
        do {
            try {
                if (driver.findElement(By.xpath("//*[contains(@label, '" + text + "')]")).isDisplayed()) {
                    return true;
                } else {
                    return false;
                }
            } catch (NoSuchElementException ignored) {
            }
        } while (System.currentTimeMillis() - start < timeout);
        return false;
    }

    @Override
    public void startVideo() {
//        throw new UnsupportedCommandException();
    }

    @Override
    public void endVideo(String dirPath, String filename) {
//        throw new UnsupportedCommandException();
    }

    @Override
    public void startLog() {
        // clear log
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File(TEMP_LOG_FILE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();
    }

    @Override
    public void endLog(String dirPath, String filename) {
        if (dirPath != null) {
            try {
                // copy log file to a new file
                runBashCommand("cp " + TEMP_LOG_FILE + " " + filename);
                Thread.sleep(1500);
                // move the new file to target dir
                FileUtils.moveFileToDirectory(new File(filename), new File(dirPath), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void acceptAlert(int timeout) {
        handleAlert(true, timeout);
    }

    @Override
    public void dismissAlert(int timeout) {
        handleAlert(false, timeout);
    }

    private void handleAlert(boolean accept, int timeout) {
        long start = System.currentTimeMillis();
        do {
            try {
                if (driver.findElement(By.className("UIAAlert")) != null) {
                    Alert alert = driver.switchTo().alert();
                    if (accept) {
                        alert.accept();
                    } else {
                        alert.dismiss();
                    }
                }
            } catch (Exception ignored) {
            }
        } while (System.currentTimeMillis() - start < timeout);
    }

    public void scrollDown() {
        Map<String, String> scrollObject = ImmutableMap.of("direction", "down");
        driver.executeScript("mobile: scroll", scrollObject);
    }
}