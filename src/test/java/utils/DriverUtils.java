package utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

@Slf4j
public class DriverUtils {

    private static final String BROWSERSTACK_HUB = "https://hub-cloud.browserstack.com/wd/hub";

    public static AndroidDriver getAndroidDriver() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();

        options.setCapability("bstack:options", new HashMap<String, Object>() {{
            put("userName",    ConfigUtils.getTestVariable("bs_username"));
            put("accessKey",   ConfigUtils.getTestVariable("bs_accessKey"));
            put("projectName", ConfigUtils.getTestVariable("bs_projectName"));
            put("buildName",   ConfigUtils.getTestVariable("bs_buildName"));
            put("sessionName", "Android Login Tests");
        }});

        options.setDeviceName(ConfigUtils.getTestVariable("android_deviceName"));
        options.setPlatformVersion(ConfigUtils.getTestVariable("android_platformVersion"));
        options.setApp(ConfigUtils.getTestVariable("android_app"));
        options.setNewCommandTimeout(Duration.ofSeconds(60));
        options.setAutoGrantPermissions(true);

        log.info("Initializing AndroidDriver on BrowserStack — device: {}", ConfigUtils.getTestVariable("android_deviceName"));
        return new AndroidDriver(new URL(BROWSERSTACK_HUB), options);
    }

    public static IOSDriver getIOSDriver() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions();

        options.setCapability("bstack:options", new HashMap<String, Object>() {{
            put("userName",    ConfigUtils.getTestVariable("bs_username"));
            put("accessKey",   ConfigUtils.getTestVariable("bs_accessKey"));
            put("projectName", ConfigUtils.getTestVariable("bs_projectName"));
            put("buildName",   ConfigUtils.getTestVariable("bs_buildName"));
            put("sessionName", "iOS Login Tests");
        }});

        options.setDeviceName(ConfigUtils.getTestVariable("ios_deviceName"));
        options.setPlatformVersion(ConfigUtils.getTestVariable("ios_platformVersion"));
        options.setApp(ConfigUtils.getTestVariable("ios_app"));
        options.setNewCommandTimeout(Duration.ofSeconds(60));
        options.setAutoAcceptAlerts(true);

        log.info("Initializing IOSDriver on BrowserStack — device: {}", ConfigUtils.getTestVariable("ios_deviceName"));
        return new IOSDriver(new URL(BROWSERSTACK_HUB), options);
    }
}
