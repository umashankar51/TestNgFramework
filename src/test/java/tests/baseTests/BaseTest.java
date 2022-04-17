package tests.baseTests;

import extentUtils.ExtentManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConfigUtils;
import utils.TestDataFactory;

import java.lang.reflect.Method;

public class BaseTest {

	protected static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite(){
		Assert.assertTrue(ConfigUtils.loadTestConfig(),
				"Asserting if Test Properties are loaded");
		Assert.assertTrue(ConfigUtils.loadEnvConfig(),
				"Asserting if Environment Properties are loaded");
		TestDataFactory.loadTestData();
	}

	@BeforeClass(alwaysRun = true)
	public void beforeParentClass() {
		LOGGER.info("Test Execution started for TestClass - "+this.getClass().getSimpleName());
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeParentMethod(final Method method) {
		LOGGER.info("Test Execution started for Test - "+method.getName());
	}

	@AfterMethod(alwaysRun = true)
	public void afterParentMethod(final Method method, final ITestResult testResult){
		LOGGER.info("Test Execution completed for Test - "+testResult.getMethod().getMethodName());
		ExtentManager.extentReports.flush();
	}

	@AfterClass(alwaysRun = true)
	public void afterParentClass(){
		LOGGER.info("Test Execution completed for TestClass - "+this.getClass().getSimpleName());
	}
}
