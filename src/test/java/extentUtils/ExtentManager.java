package extentUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import utils.ConfigUtils;

public class ExtentManager {

    public static final ExtentReports extentReports = new ExtentReports();
    public synchronized static ExtentReports createExtentReports() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("TestReport.html");
        reporter.config().setReportName("Salesforce Regression Test Report");
        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("environment", ConfigUtils.getTestVariable("testenv"));
        extentReports.setSystemInfo("groups_included", ConfigUtils.getTestVariable("groups"));
        extentReports.setSystemInfo("groups_excluded", ConfigUtils.getTestVariable("excludedGroups"));
        extentReports.setSystemInfo("headless", ConfigUtils.getTestVariable("headless"));
        return extentReports;
    }
}