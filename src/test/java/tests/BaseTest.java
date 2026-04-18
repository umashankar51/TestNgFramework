package tests;

import com.aventstack.extentreports.Status;
import extentUtils.ExtentManager;
import extentUtils.ExtentTestManager;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.BrowserStackUtils;
import utils.ConfigUtils;
import utils.DriverUtils;
import utils.TestDataFactory;
import utils.XrayUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class BaseTest {

    protected AppiumDriver driver;

    /** Collects per-test recording + result data; processed in {@code afterParentClass} after driver quits. */
    private final List<TestRecord> testRecords = Collections.synchronizedList(new ArrayList<>());

    // ── Suite ─────────────────────────────────────────────────────────────────

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        Assert.assertTrue(ConfigUtils.loadTestConfig(),
                "Asserting if Test Properties are loaded");
        Assert.assertTrue(ConfigUtils.loadEnvConfig(),
                "Asserting if Environment Properties are loaded");
        TestDataFactory.loadTestData();

        // Authenticate with Xray and resolve / create execution key
        String buildId = System.getenv("BUILD_BUILDID");
        XrayUtils.init(buildId);
    }

    // ── Class ─────────────────────────────────────────────────────────────────

    @BeforeClass(alwaysRun = true)
    public void beforeParentClass() throws Exception {
        log.info("Test Execution started for TestClass - {}", this.getClass().getSimpleName());
        String platform = ConfigUtils.getTestVariable("platform");
        if (platform != null && !platform.isBlank()) {
            log.info("Initializing driver for platform: {}", platform);
            driver = "ios".equalsIgnoreCase(platform)
                    ? DriverUtils.getIOSDriver()
                    : DriverUtils.getAndroidDriver();
            log.info("Driver initialised successfully");
        }
    }

    // ── Method ────────────────────────────────────────────────────────────────

    @BeforeMethod(alwaysRun = true)
    public void beforeParentMethod(final Method method) {
        log.info("Test Execution started for Test - {}", method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void afterParentMethod(final Method method, final ITestResult testResult) {
        log.info("Test Execution completed for Test - {}", testResult.getMethod().getMethodName());

        // Screenshot on failure
        if (driver != null && testResult.getStatus() == ITestResult.FAILURE) {
            try {
                String base64Screenshot = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BASE64);
                ExtentTestManager.getTest()
                        .log(Status.FAIL, testResult.getThrowable())
                        .addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
            } catch (Exception e) {
                log.error("Failed to capture screenshot: {}", e.getMessage());
            }
        }
        ExtentManager.extentReports.flush();

        // Queue this test's data for Xray processing after the driver session ends
        if (XrayUtils.isEnabled() && driver != null) {
            String jiraKey   = testResult.getMethod().getDescription();
            String sessionId = driver.getSessionId().toString();
            testRecords.add(new TestRecord(jiraKey, sessionId, testResult.getStatus()));
        }
    }

    // ── Class teardown — process Xray after driver quits ─────────────────────

    @AfterClass(alwaysRun = true)
    public void afterParentClass() {
        log.info("Test Execution completed for TestClass - {}", this.getClass().getSimpleName());

        if (driver != null) {
            driver.quit();
            log.info("Driver session closed");
        }

        // BrowserStack video is only available after the session ends — process now
        if (XrayUtils.isEnabled() && !testRecords.isEmpty()) {
            for (TestRecord record : testRecords) {
                processXrayReport(record);
            }
        }
    }

    // ── private helpers ───────────────────────────────────────────────────────

    private void processXrayReport(TestRecord record) {
        String status = toXrayStatus(record.testNgStatus());

        // 1. Fetch BrowserStack video URL (polls until ready)
        String videoUrl = BrowserStackUtils.getVideoUrl(record.sessionId());

        // 2. Download the recording
        File videoFile = null;
        if (videoUrl != null) {
            videoFile = BrowserStackUtils.downloadVideo(record.sessionId(), videoUrl);
        }

        // 3. Add test to execution, upload evidence, update status
        XrayUtils.reportTestResult(record.jiraKey(), status, videoFile);

        // 4. Clean up temp file
        if (videoFile != null) videoFile.delete();
    }

    private String toXrayStatus(int testNgStatus) {
        return switch (testNgStatus) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            default                  -> "SKIPPED";
        };
    }

    /** Holds per-test data collected in {@code afterParentMethod} for deferred processing. */
    private record TestRecord(String jiraKey, String sessionId, int testNgStatus) {}
}
