package tests.baseTests;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import utils.ConfigUtils;

public class BaseUITest extends BaseTest {

	private static final Logger LOGGER = LogManager.getLogger(BaseUITest.class);

	protected WebDriver driver;

	@BeforeClass(alwaysRun = true)
	public void beforeChildClass() {
		if(driver == null) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized");
			options.addArguments("disable-infobars");
			options.addArguments("--disable-extensions");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--no-sandbox");
			if("true".equalsIgnoreCase(ConfigUtils.getTestVariable("headless"))){
				options.addArguments("--headless");
			}
			if(System.getProperty("os.name").contains("ux")) {
				System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
			}else {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
			}
			driver = new ChromeDriver(options);
			LOGGER.info("Webdriver launched with headless mode: "+ConfigUtils.getTestVariable("headless"));
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterChildClass(){
		if(driver != null) {
			try {
				driver.quit();
				LOGGER.info("Webdriver closed successfully!!");
			}catch (Exception e){
				//e.printStackTrace();
				LOGGER.info("Error quitting webdriver: "+e.getLocalizedMessage());
			}
		}
	}

	/*@AfterMethod(alwaysRun = true)
	public void afterMethod(){
		Allure.addAttachment("screenshot", new ByteArrayInputStream(screenshot()));
	}

	@Attachment(value = "Screenshot", type = "image/png")
	public byte[] screenshot() {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	}*/
}
