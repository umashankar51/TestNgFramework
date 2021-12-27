package pageobjects.google;

import constants.MessageConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import pageobjects.BasePage;

public class GoogleCaptchaErrorPage extends BasePage {

    private static final Logger LOGGER = LogManager.getLogger(GoogleCaptchaErrorPage.class);

    public GoogleCaptchaErrorPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver,this);
    }

    @FindBy (id="recaptcha-anchor-label")
    public WebElement captchaError;

    public void validateCaptchaError(){
        wait.until(ExpectedConditions.elementToBeClickable(captchaError));
        Assert.assertEquals(captchaError.getText(), MessageConstants.CAPTCHA_ERROR,
                "Asserting if Captcha Error is displayed");
    }

    public boolean isCaptchaPresent(){
        try {
            wait.until(ExpectedConditions.visibilityOf(captchaError));
            return true;
        }catch(Exception e) {
            //e.printStackTrace();
            LOGGER.info("Captch Error was not displayed");
            return false;
        }
    }
}
