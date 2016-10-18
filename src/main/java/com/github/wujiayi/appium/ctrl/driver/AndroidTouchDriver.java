package com.github.wujiayi.appium.ctrl.driver;

import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.RemoteTouchScreen;

import io.appium.java_client.android.AndroidDriver;

/**
 * 
 * Customized android driver that implements touch actions
 * 
 * @author chris.wu
 *
 */
public class AndroidTouchDriver extends AndroidDriver<WebElement> implements HasTouchScreen {

	public RemoteTouchScreen touch;

	public AndroidTouchDriver(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
		touch = new RemoteTouchScreen(getExecuteMethod());
	}

	public TouchScreen getTouch() {
		return touch;
	}
}