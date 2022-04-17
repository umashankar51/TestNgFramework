package listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import extentUtils.ExtentManager;
import extentUtils.ExtentTestManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import pageobjects.democart.HomePage;
import tests.baseTests.BaseUITest;

public class TestReportListener extends BaseUITest implements ITestListener {

    private static final Logger LOGGER = LogManager.getLogger(TestReportListener.class);

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Override
    public void onStart(ITestContext iTestContext) {
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        LOGGER.info("I am in onFinish method " + iTestContext.getName());
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        ExtentTestManager.startTest(getTestMethodName(iTestResult), getTestMethodName(iTestResult));
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        ExtentTestManager.getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        LOGGER.info(getTestMethodName(iTestResult) + " test is failed.");
        ExtentTestManager.getTest().log(Status.FAIL, iTestResult.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        LOGGER.info(getTestMethodName(iTestResult) + " test is skipped.");
        ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        LOGGER.info("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
    }
}