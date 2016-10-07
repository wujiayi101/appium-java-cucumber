package com.github.wujiayi.appium.example;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * This is the entry point of the whole test project
 *
 * Can be invoked by 'mvn install'
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"json:target/cucumber.json", "rerun:target/rerun.txt"}, features = "src/test/resources/features")
public class CukeTest {

    public static Logger logger = LoggerFactory.getLogger(CukeTest.class.getSimpleName());
    private static String app = null;

    @BeforeClass
    public static void beforeClass() {
        logger.info("@BeforeClass:beforeClass...");

        // clean up output folders
        try {
            FileUtils.deleteDirectory(new File("output"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null == System.getProperty("app", null)) {
            Assert.fail("Invalid app path: " + app);
        }
    }

    @AfterClass
    public static void afterClass() {
        try {
            // clear jenkins env file
            FileUtils.write(new File("jenkins_env.properties"), "");

            // appends test summary to jenkins env
            FileUtils.write(new File("jenkins_env.properties"), TestSummary.getInstance().getTestSummary(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}