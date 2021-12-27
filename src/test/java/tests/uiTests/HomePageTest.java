package tests.uiTests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageobjects.democart.HomePage;
import tests.baseTests.BaseUITest;
import utils.ConfigUtils;

public class HomePageTest extends BaseUITest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void beforeChildMethod(){
        homePage = new HomePage(driver);
    }

    @Test(groups= {"menuOptionsTest","regression","uiTests"})
    public void tc_p1_validate_HomePage_test(){
        driver.get(ConfigUtils.getEnvVariable("UI_HOME"));
        homePage.validateMenuBarElements();
        homePage.validateHomePageLabel();
    }
}
