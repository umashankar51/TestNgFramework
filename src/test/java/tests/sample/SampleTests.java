package tests.sample;

import org.testng.Assert;
import org.testng.annotations.Test;
import tests.BaseTest;

public class SampleTests extends BaseTest {

    @Test(groups = "sample", description = "SAMPLE-001")
    public void testPassed() {
        Assert.assertTrue(true, "This test should pass");
    }

    @Test(groups = "sample", description = "SAMPLE-002")
    public void testFailed() {
        Assert.fail("This test is intentionally failing");
    }
}
