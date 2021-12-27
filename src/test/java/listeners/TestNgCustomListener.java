package listeners;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestNgCustomListener implements ITestListener {
    /**************** logger **************/
    private static final Logger LOGGER = LogManager.getLogger(TestNgCustomListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        //TO-DO
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        //TO-DO
    }

    @Override
    public void onTestFailure(ITestResult result){
        LOGGER.error("Error on test - " + result.getName());
        if(result.getThrowable()!=null){
            result.getThrowable().printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        //TO-DO
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        //TO-DO
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        //TO-DO
    }

    @Override
    public void onStart(ITestContext context) {
        //TO-DO
    }

    @Override
    public void onFinish(ITestContext context) {
        //TO-DO
    }
}
