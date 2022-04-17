package tests.uiTests;

import com.aventstack.extentreports.Status;
import extentUtils.ExtentTestManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageobjects.democart.HomePage;
import pageobjects.democart.SearchResultsPage;
import tests.baseTests.BaseUITest;

public class EllipsisTest extends BaseUITest {


    @Test(groups= {"ellipsisTest","regression","uiTests"})
    public void tc_p1_ellipsis_test() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        System.out.println("Ellipsis");
        ExtentTestManager.getTest().log(Status.PASS, "SOP printed");
        driver.get("https://codepen.io/DesignerAshishOrg/pen/JoYZOB");
        ExtentTestManager.getTest().log(Status.PASS, "Page Navigated");
        //Thread.sleep(15000);
        System.out.println(driver.findElement(By.xpath("//body[@translate='no']/div")).getText());
    }
}
