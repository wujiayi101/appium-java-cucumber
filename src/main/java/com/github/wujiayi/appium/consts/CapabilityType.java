package com.github.wujiayi.appium.consts;

import io.appium.java_client.remote.MobileCapabilityType;

/**
 * 
 * Extends mobile capability type
 * 
 * @author chris.wu
 *
 */
public interface CapabilityType extends MobileCapabilityType {

	  // android only
	  String RESET_KEYBOARD = "resetKeyboard";
	  
	  // iOS only
	  String NATIVE_INSTRUMENTS_LIB = "nativeInstrumentsLib";
	  String AUTO_ACCEPT_ALERTS = "autoAcceptAlerts";
	  String WAIT_FOR_APP_SCRIPT = "waitForAppScript";

	  int DEFAULT_WAIT_TIME = 20000;

}
