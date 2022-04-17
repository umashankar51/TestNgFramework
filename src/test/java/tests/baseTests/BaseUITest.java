package tests.baseTests;

import extentUtils.ExtentTestManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import utils.ConfigUtils;

public class BaseUITest extends BaseTest {

	private static final Logger LOGGER = LogManager.getLogger(BaseUITest.class);

	protected WebDriver driver;

	@BeforeClass(alwaysRun = true)
	public void beforeChildClass() {
		try {
			if (driver == null) {
				ChromeOptions options = new ChromeOptions();
				options.addArguments("start-maximized");
				options.addArguments("disable-infobars");
				options.addArguments("--disable-extensions");
				options.addArguments("--disable-dev-shm-usage");
				options.addArguments("--no-sandbox");
				if ("true".equalsIgnoreCase(ConfigUtils.getTestVariable("headless"))) {
					options.addArguments("--headless");
				}
				if (System.getProperty("os.name").contains("ux")) {
					System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
				} else {
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
				}
				driver = new ChromeDriver(options);
				LOGGER.info("Webdriver launched with headless mode: " + ConfigUtils.getTestVariable("headless"));
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterChildClass(){
		try {
			if(driver != null) {
				try {
					driver.quit();
					LOGGER.info("Webdriver closed successfully!!");
				}catch (Exception e){
					//e.printStackTrace();
					LOGGER.info("Error quitting webdriver: "+e.getLocalizedMessage());
				}
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(final ITestResult result){
		try {
			if (result.getStatus() == ITestResult.FAILURE) {
				String base64Screenshot =
						"data:image/png;base64," + ((TakesScreenshot) (driver)).getScreenshotAs(OutputType.BASE64);
				ExtentTestManager.getTest().addScreenCaptureFromBase64String(base64Screenshot).getModel().getMedia().get(0);
			}
		}catch (Throwable t){
			t.printStackTrace();
		}
	}
}
