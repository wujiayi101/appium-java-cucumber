package com.github.wujiayi.appium.example.steps;

import com.github.wujiayi.appium.screens.cuke.Cuke;
import cucumber.api.java.en.Then;

/**
 * Created by chris_wu on 22/2/2016.
 */
public class StepDefinitions {

    @Then("^on welcome screen$")
    public void on_welcome_screen() throws Throwable {
        Cuke.welcomeScreen.waitForScreen();
    }
}
