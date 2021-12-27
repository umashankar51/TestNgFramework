package tests.uiTests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageobjects.google.GoogleCaptchaErrorPage;
import pageobjects.google.GoogleHomePage;
import pageobjects.google.GoogleImageResultsPage;
import pageobjects.google.GoogleImageSearchPage;
import tests.baseTests.BaseUITest;

public class FileUploadTest extends BaseUITest {

    private GoogleHomePage googleHomePage;
    private GoogleImageSearchPage googleImageSearchPage;
    private GoogleCaptchaErrorPage googleCaptchaErrorPage;
    private GoogleImageResultsPage googleImageResultsPage;

    @BeforeMethod(alwaysRun = true)
    public void beforeChildMethod(){
        googleHomePage = new GoogleHomePage(driver);
        googleImageSearchPage = new GoogleImageSearchPage(driver);
        googleCaptchaErrorPage = new GoogleCaptchaErrorPage(driver);
        googleImageResultsPage = new GoogleImageResultsPage(driver);
    }

    @Test(groups= {"fileUploadTest","regression","uiTests", "broken"})
    public void fileUploadTest()  {
        driver.get("http://google.com/");
        googleHomePage.navigateToGoogleImages();
        googleImageSearchPage.uploadGoogleImage("FarCry6.jpg");
        if(googleCaptchaErrorPage.isCaptchaPresent()) {
            googleCaptchaErrorPage.validateCaptchaError();
        }else{
            googleImageResultsPage.validateImageResults();
        }
    }
}
