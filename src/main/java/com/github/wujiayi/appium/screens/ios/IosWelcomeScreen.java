package com.github.wujiayi.appium.screens.ios;

import com.github.wujiayi.appium.screens.WelcomeScreen;
import org.openqa.selenium.By;

/**
 * Created by chris_wu on 22/2/2016.
 */
public class IosWelcomeScreen extends WelcomeScreen {

    private static final By txt_title = By.id("welcome_label");
    private static final By txt_description = By.id("welcome_health_life_label");
    private static final By btn_start = By.id("welcome_start_to_use_button");
    private static final By img_screen_view = By.id("welcome_screen_1");

    private static final By[] atThis = new By[]{btn_start};

    @Override
    public void clickStart() {
        mobileCtrl.click(btn_start);
    }

    @Override
    public String getTitle(int screenIdx) {
        return mobileCtrl.getText(txt_title);
    }

    @Override
    public String getDescription(int screenIdx) {
        return mobileCtrl.getText(txt_description);
    }

    @Override
    public void waitForScreen(int timeout) {
        waitForScreen(atThis, timeout);
    }
}
