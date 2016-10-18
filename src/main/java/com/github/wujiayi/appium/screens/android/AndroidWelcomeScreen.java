package com.github.wujiayi.appium.screens.android;

import com.github.wujiayi.appium.screens.WelcomeScreen;
import org.openqa.selenium.By;

/**
 * Created by chris_wu on 22/2/2016.
 */
public class AndroidWelcomeScreen extends WelcomeScreen {

    private static final By txt_title = By.id("tv_title");
    private static final By txt_description = By.id("tv_description");
    private static final By btn_start = By.id("btn_start");
    private static final By img_screen_view = By.id("viewpager");

    private static final By[] atThis = new By[]{btn_start};

    @Override
    public void clickStart() {
        mobileCtrl.click(btn_start);
    }

    @Override
    public String getTitle(int screenIdx) {
        return mobileCtrl.findElements(txt_title).get(screenIdx).getText();
    }

    @Override
    public String getDescription(int screenIdx) {
        return mobileCtrl.findElements(txt_description).get(screenIdx).getText();
    }

    @Override
    public void waitForScreen(int timeout) {
        waitForScreen(atThis, timeout);
    }
}
