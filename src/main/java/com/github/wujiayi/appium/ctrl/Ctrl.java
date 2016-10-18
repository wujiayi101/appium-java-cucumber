package com.github.wujiayi.appium.ctrl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.wujiayi.appium.consts.CapabilityType;
import com.github.wujiayi.appium.consts.MobileCtrlException;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.appium.java_client.AppiumDriver;

/**
 * @author chris.wu
 */
public abstract class Ctrl {

    protected final static Logger logger = LoggerFactory.getLogger(Ctrl.class.getSimpleName());
    protected AppiumDriver<WebElement> driver = null;

    /**
     * @param by identifier of the text field to be cleared
     */
    public void clearText(By by) {
        clearText(by, CapabilityType.DEFAULT_WAIT_TIME);
    }

    /**
     * @param by      identifier of the text field to be cleared
     * @param timeout try until [timeout] seconds
     */
    public abstract void clearText(By by, int timeout);

    /**
     * @param elem    WebElement text field to be clear
     * @param timeout try until [timeout] seconds
     */
    public abstract void clearText(WebElement elem, int timeout);

    /**
     * @param by identifier of the component to be clicked
     */
    public void click(By by) {
        click(by, CapabilityType.DEFAULT_WAIT_TIME);
    }

    /**
     * @param by      identifier of the component to be clicked
     * @param timeout try until [timeout] seconds
     */
    public void click(By by, int timeout) {
        long start = System.currentTimeMillis();

        do {
            WebElement webElement = findElement(by, timeout);

            logger.debug(String.format("click %s", by.toString()));
            try {
                webElement.click();
                return;
            } catch (RuntimeException ignored) {
            }
        } while (System.currentTimeMillis() - start < timeout);

        throw new MobileCtrlException(String.format("element \"%s\" can not be clicked after %d ms", by.toString(), timeout));
    }

    /**
     * @param by             identifier of the component to be clicked
     * @param checkDisplayed check displayed and enabled after find
     * @param index          duplicate id index
     * @param timeout        try until [timeout] seconds
     */
    public void click(By by, boolean checkDisplayed, int index, int timeout) {
        long start = System.currentTimeMillis();

        do {
            List<WebElement> elements = findElements(by, checkDisplayed, timeout);

            logger.debug(String.format("click %s[%d]", by.toString(), index));
            try {
                elements.get(index).click();
                return;
            } catch (RuntimeException e) {
            }
        } while (System.currentTimeMillis() - start < timeout);

        throw new MobileCtrlException(String.format("element \"%s\" can not be clicked after %d ms", by.toString(), timeout));
    }

    /**
     * @param by identifier of the component to be clicked, the last found
     *           element will be clicked
     */
    public void clickLast(By by) {
        clickLast(by, CapabilityType.DEFAULT_WAIT_TIME);
    }

    /**
     * @param by      identifier of the component to be clicked, the last found
     *                element will be clicked
     * @param timeout try until [timeout] seconds
     */
    public void clickLast(By by, int timeout) {
        List<WebElement> elements = findElements(by, timeout);

        logger.debug(String.format("clickLast %s", by.toString()));
        try {
            elements.get(elements.size() - 1).click();
        } catch (RuntimeException e) {
            throw new MobileCtrlException(
                    String.format("element \"%s\" can not be clicked after %d ms", by.toString(), timeout));
        }
    }

    /**
     * @param by identifier of the text field to be clicked
     */
    public void clickTextField(By by) {
        clickTextField(by, CapabilityType.DEFAULT_WAIT_TIME);
    }

    /**
     * @param by      identifier of the text field to be clicked
     * @param timeout try until [timeout] seconds
     */
    public abstract void clickTextField(By by, int timeout);

    /**
     * @param by   identifier of the text field
     * @param text string value to be entered
     */
    public void enterText(By by, String text) {
        enterText(by, text, CapabilityType.DEFAULT_WAIT_TIME);
    }

    /**
     * @param by   identifier of the text field
     * @param text string value to be entered
     */
    public void enterText(By by, String text, int timeout) {
        enterText(by, text, timeout, false);
    }

    /**
     * @param by      identifier of the text field
     * @param text    string value to be entered
     * @param type    type characters one by one
     * @param timeout try until [timeout] seconds
     */
    public abstract void enterText(By by, String text, int timeout, boolean type);

    /**
     * @param parentBy identifier of the parent element
     * @param childBy  identifier of the text field
     * @param text     string value to be entered
     */
    public void enterText(By parentBy, By childBy, String text) {
        enterText(parentBy, childBy, text, CapabilityType.DEFAULT_WAIT_TIME);
    }

    /**
     * @param parentBy identifier of the parent element
     * @param childBy  identifier of the text field
     * @param text     string value to be entered
     * @param timeout
     */
    public void enterText(By parentBy, By childBy, String text, int timeout) {
        enterText(parentBy, childBy, text, timeout, false);
    }

    /**
     * @param parentBy identifier of the parent element
     * @param childBy  identifier of the text field
     * @param text     string value to be entered
     * @param type     type characters one by one
     * @param timeout  try until [timeout] seconds
     */
    public abstract void enterText(By parentBy, By childBy, String text, int timeout, boolean type);

    /**
     * @param elem    the text field WebElement
     * @param text    string value to be entered
     * @param type    type characters one by one
     * @param timeout try until [timeout] seconds
     */
    public abstract boolean enterText(WebElement elem, String text, int timeout, boolean type);

    /**
     * @param by identifier of the component to be found
     * @return the webElement found
     */
    public WebElement findElement(By by) {
        return findElement(by, CapabilityType.DEFAULT_WAIT_TIME);
    }

    /**
     * @param by      identifier of the component to be found
     * @param timeout try until [timeout] milliseconds
     * @return the webElement found
     */
    public WebElement findElement(By by, int timeout) {
        return findElement((WebElement) null, by, timeout);
    }

    /**
     * @param parentBy identifier of the component to be found
     * @param childBy  identifier of the component to be found under parentBy
     * @return the webElement found
     */
    public WebElement findElement(By parentBy, By childBy) {
        return findElement(parentBy, childBy, CapabilityType.DEFAULT_WAIT_TIME);
    }

    /**
     * @param parentBy identifier of the component to be found
     * @param childBy  identifier of the component to be found under parentBy
     * @param timeout  try until [timeout] milliseconds
     * @return the webElement found
     */
    public abstract WebElement findElement(By parentBy, By childBy, int timeout);

    /**
     * @param elem    the parent element, null if find from root
     * @param by      the child By object
     * @param timeout try until [timeout] milliseconds
     * @return the WebElement found
     */
    public abstract WebElement findElement(WebElement elem, By by, int timeout);

    /**
     * @param elem           the parent element, null if find from root
     * @param by             the child By object
     * @param checkDisplayed check isDisplayed after findElement
     * @param timeout        try until [timeout] milliseconds
     * @return the WebElement found
     */
    public WebElement findElement(WebElement elem, By by, boolean checkDisplayed, int timeout) {

        List<WebElement> elems = findElements(elem, by, checkDisplayed, timeout);

        if (null == elems)
            return null;
        else
            return elems.get(0);
    }

    /**
     * @param by identifier of the component to be found
     * @return the webElements found
     */
    public List<WebElement> findElements(By by) {
        return findElements(by, CapabilityType.DEFAULT_WAIT_TIME);
    }

    /**
     * @param by      identifier of the component to be found
     * @param timeout try until [timeout] seconds
     * @return the webElements found
     */
    public abstract List<WebElement> findElements(By by, int timeout);

    /**
     * @param by             identifier of the component to be found
     * @param checkDisplayed check displayed and enabled after find
     * @param timeout        try until [timeout] seconds
     * @return the webElements found
     */
    public List<WebElement> findElements(By by, boolean checkDisplayed, int timeout) {
        return findElements(null, by, checkDisplayed, timeout);
    }

    /**
     * @param elem        the parent elem
     * @param by          identifier of the component to be found
     * @param isDisplayed check displayed and enabled after find
     * @param timeout     try until [timeout] seconds
     * @return list of WebElements
     */
    public List<WebElement> findElements(WebElement elem, By by, boolean isDisplayed, int timeout) {
        if (null == by) {
            return null;
        }

        logger.debug(String.format("findElements %s", by.toString()));

        long start = System.currentTimeMillis();
        List<WebElement> webElements = null;
        List<WebElement> outElements = null;

        do {
            if (null == elem) {
                webElements = driver.findElements(by);
            } else {
                webElements = elem.findElements(by);
            }

            if (isDisplayed) {
                outElements = getDisplayedElements(webElements);
            } else {
                outElements = webElements;
            }

            if (null != outElements && !outElements.isEmpty()) {
                return outElements;
            }

        } while (System.currentTimeMillis() - start < timeout);

        throw new MobileCtrlException(String.format("elements \"%s\" not found %safter %d ms", by.toString(),
                isDisplayed ? "or not displayed " : "", timeout));
    }

    /**
     * return the list of elements that is enabled and is displayed
     *
     * @param webElements
     * @return outElements
     */
    private List<WebElement> getDisplayedElements(List<WebElement> webElements) {

        List<WebElement> outElements = new ArrayList<WebElement>();

        try {
            if ((null == webElements) || webElements.isEmpty())
                return null;

            for (WebElement we : webElements)
                try {
                    if (we.isDisplayed() && we.isEnabled()) {
                        outElements.add(we);
                    }
                } catch (Exception ignored) {
                    // do nothing
                    // probably the web element is not enabled/displayed
                }

            if (outElements.size() > 0)
                return outElements;

        } catch (RuntimeException e1) {
            return null;
        }
        return null;
    }

    /**
     * @param by identifier of the component to be found
     * @return the text value of the component
     */
    public String getText(By by) {
        return getText(by, CapabilityType.DEFAULT_WAIT_TIME);
    }

    /**
     * @param by      identifier of the component to be found
     * @param timeout try until [timeout] seconds
     * @return the text value of the component
     */
    public abstract String getText(By by, int timeout);

    /**
     * @param parentBy identifier of the component to be found
     * @param childBy  identifier of the component to be found under parentBy
     * @return the text value of the component
     */
    public String getText(By parentBy, By childBy) {
        return getText(parentBy, childBy, CapabilityType.DEFAULT_WAIT_TIME);
    }

    /**
     * @param parentBy identifier of the component to be found
     * @param childBy  identifier of the component to be found under parentBy
     * @param timeout  try until [timeout] seconds
     * @return the text value of the component
     */
    public abstract String getText(By parentBy, By childBy, int timeout);

    /**
     * @param elem the element to get text from
     * @return the text value of the component
     */
    public abstract String getText(WebElement elem);

    public abstract void sendKeyEvent(CharSequence... keyToSend);

    /**
     * @param appPath full path of the .apk (Android) or .app (iOS)
     */
    public abstract void setUp(String appPath);

    public void tearDown() {
        if (null != driver) {
            driver.quit();
        }
    }

    public void reset() {
        if (null != driver) {
            driver.resetApp();
        }
    }

    /**
     * hide the keyboard if it is showing
     */
    public void hideKeyboard() {
        try {
            driver.hideKeyboard();
        } catch (WebDriverException e) {
            logger.debug("Keyboard is not showing, cannot run hidekeyboard() !");
        }
    }

    /**
     * recursive find element, traverse the children top down from root
     *
     * @param children By objects top down, the last one is the final target
     * @param timeout  try until [timeout] seconds
     * @return the element found
     */
    public WebElement recursiveFindElement(List<By> children, int timeout) {
        return recursiveFindElement(null, children, timeout);
    }

    /**
     * recursive find element, traverse the children top down from parent
     *
     * @param parent   findElement under parent, if null, find from root
     * @param children By objects top down, the last one is the final target
     * @param timeout  try until [timeout] seconds
     * @return the element found
     */
    public WebElement recursiveFindElement(WebElement parent, List<By> children, int timeout) {
        if (null == parent)
            logger.debug(String.format("recursiveFindElement %s", children.get(0).toString()));
        else
            logger.debug(String.format("recursiveFindElement %s %s", parent.toString(), children.get(0).toString()));

        long start = System.currentTimeMillis();
        do {
            try {
                if (null == parent)
                    parent = driver.findElement(children.get(0));
                else
                    parent = parent.findElement(children.get(0));

                if (1 == children.size())
                    return parent;
                else
                    return recursiveFindElement(parent, children.subList(1, children.size()), timeout);
            } catch (RuntimeException ignored) {
            }
        } while (System.currentTimeMillis() - start < timeout);

        throw new MobileCtrlException(
                String.format("element \"%s\" not found after %d ms", children.get(0).toString(), timeout));
    }

    /**
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void click(int x, int y) {
        logger.info(String.format("click %d %d", x, y));

        try {
            driver.tap(1, x, y, 1);
        } catch (RuntimeException e) {
            throw new MobileCtrlException(e);
        }
    }

    public abstract boolean verifyText(String text, int timeout);

    public void takeScreenshot(String fileName, String dir) {
        String platform = System.getProperty("platform", MobilePlatform.ANDROID);
        File f = driver.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(f, new File(dir + File.separator + platform + File.separator + fileName));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * run a string array of command
     *
     * @param command
     */
    protected Process runBashCommand(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            e.printStackTrace();
            if (null != process) {
                process.destroy();
            }
        }
        return process;
    }

    public abstract void startVideo();

    public abstract void endVideo(String dirPath, String filename);

    public abstract void startLog();

    public abstract void endLog(String dirPath, String filename);

    public abstract void acceptAlert(int timeout);

    public abstract void dismissAlert(int timeout);

    public void hideKeyboard(int timeout) {
        driver.hideKeyboard();
    }

}