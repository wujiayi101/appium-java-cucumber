package com.github.wujiayi.appium.example.screens.cuke;

import com.github.wujiayi.appium.example.screens.WelcomeScreen;
import com.github.wujiayi.appium.example.screens.android.AndroidWelcomeScreen;
import com.github.wujiayi.appium.example.screens.ios.IosWelcomeScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.appium.java_client.remote.MobilePlatform;

/**
 * Initialize all screens and holds the references to all screen objects
 *
 * @author chris.wu
 */
public class Cuke {

    private static Logger logger = LoggerFactory.getLogger(Cuke.class.getSimpleName());

    // references to all screen objects
    public static WelcomeScreen welcomeScreen;

    /**
     * create all screen objects
     */
    static {
        String platform = System.getProperty("platform", MobilePlatform.IOS);

        logger.info("Start initializing screen objects ...");

        if (platform.equals(MobilePlatform.ANDROID)) {
            welcomeScreen = new AndroidWelcomeScreen();
        } else {
            welcomeScreen = new IosWelcomeScreen();
        }

        logger.info("Done initializing screen objects ...");
    }

    /**
     * init driver
     *
     * @param platform
     * @param app
     */
    public static void setUp(String platform, String app) {
        welcomeScreen.setUp(platform, app);
    }

    public static void tearDown() {
        welcomeScreen.tearDown();
    }

}
