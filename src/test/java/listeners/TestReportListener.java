package listeners;

import com.aventstack.extentreports.Status;
import extentUtils.ExtentTestManager;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class TestReportListener implements ITestListener {

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Override
    public void onStart(ITestContext iTestContext) {
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        log.info("I am in onFinish method {}", iTestContext.getName());
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
        log.info("{} test is failed.", getTestMethodName(iTestResult));
        ExtentTestManager.getTest().log(Status.FAIL, iTestResult.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        log.info("{} test is skipped.", getTestMethodName(iTestResult));
        ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        log.info("Test failed but it is in defined success ratio {}", getTestMethodName(iTestResult));
    }
}
