package runners;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.testng.Assert;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;
import utils.ConfigUtils;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class TestRunner {

    @Test
    public void runTestNgSuite(){
        // Custom TestNg runner using JUnit Test
        TestNG testNg = new TestNG();
        List<String> suites = new ArrayList<>();
        String testNgPath = ClassLoader.getSystemClassLoader().getSystemResource("testng.xml").getPath();
        if(!testNgPath.contains(".jar!")){
            suites.add(testNgPath);
        }else{
            try {
                testNg.setTestJar(TestRunner.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
                        .getPath());
                testNg.setXmlPathInJar("testng.xml");
            }catch (URISyntaxException e){
                e.printStackTrace();
            }
        }
        Assert.assertTrue(ConfigUtils.loadTestConfig(),
                "Asserting if Test Properties are loaded");
        testNg.setTestSuites(suites);
        testNg.setConfigFailurePolicy(XmlSuite.FailurePolicy.CONTINUE);
        testNg.setGroups(ConfigUtils.getTestVariable("groups_included"));
        testNg.setExcludedGroups(ConfigUtils.getTestVariable("groups_excluded"));
        try{
            testNg.run();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
