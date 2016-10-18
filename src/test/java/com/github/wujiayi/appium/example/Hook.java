package com.github.wujiayi.appium.example;


import com.github.wujiayi.appium.screens.cuke.Cuke;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.appium.java_client.remote.MobilePlatform;

/**
 * Before test setup and after test tear down
 *
 * @author chris.wu
 */
public class Hook {

    @After
    public void afterScenario(Scenario scenario) {
        String platform = System.getProperty("platform", MobilePlatform.ANDROID);
        // tear down by kill the driver
        CukeTest.logger.info(String.format("Finished scenario [%s], test status: [%s]", scenario.getName(), scenario.getStatus()));

        Cuke.tearDown();

        String targetDir = String.format("output/%s", platform);
        String videoFileName = String.format("[%s]_%s.mp4", scenario.getStatus(), scenario.getName().replace(" ", "_"));
        String logFileName = String.format("[%s]_%s.log", scenario.getStatus(), scenario.getName().replace(" ", "_"));

        Cuke.welcomeScreen.endVideo(targetDir, videoFileName);
        Cuke.welcomeScreen.endLog(targetDir, logFileName);
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        CukeTest.logger.info("---------------------------------------------------------");
        CukeTest.logger.info(String.format("Starting scenario [%s]", scenario.getName()));
        // create new driver before every test case (reset)
        String platform = System.getProperty("platform", MobilePlatform.ANDROID);
        Cuke.setUp(platform, System.getProperty("app"));

        Cuke.welcomeScreen.startVideo();
        Cuke.welcomeScreen.startLog();
    }

}
