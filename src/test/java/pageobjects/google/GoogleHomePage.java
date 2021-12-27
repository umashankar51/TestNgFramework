package pageobjects.google;

import constants.MessageConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import pageobjects.BasePage;
import utils.CommonUtils;

public class GoogleHomePage extends BasePage {

    private static final Logger LOGGER = LogManager.getLogger(GoogleHomePage.class);

    public GoogleHomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver,this);
    }

    @FindBy (xpath="//*[text()='Images']")
    public WebElement googleImagesMenu;

    @FindBy (xpath="//*[@aria-label='Search by image']")
    public WebElement searchByImageIcon;

    @FindBy (xpath="//*[@id='dRSWfb']/div/a[text()='Upload an image']")
    public WebElement uploadImage;

    @FindBy (xpath="//input[@type='file']")
    public WebElement choseFileButton;

    @FindBy (id="recaptcha-anchor-label")
    public WebElement captchaError;

    public void navigateToGoogleImages(){
        LOGGER.info("Google Images Menu is displayed");
        wait.until(ExpectedConditions.visibilityOf(googleImagesMenu));
        googleImagesMenu.click();
        LOGGER.info("Google Images Menu is clicked");
        wait.until(ExpectedConditions.visibilityOf(searchByImageIcon));
    }

    public void uploadGoogleImage(String fileName){
        String filePath = CommonUtils.getFilePath(fileName);
        if(StringUtils.isNotEmpty(filePath)){
            wait.until(ExpectedConditions.visibilityOf(searchByImageIcon));
            wait.until(ExpectedConditions.elementToBeClickable(searchByImageIcon));
            searchByImageIcon.click();
            LOGGER.info("Google Search Images Button is clicked");

            wait.until(ExpectedConditions.visibilityOf(uploadImage));
            wait.until(ExpectedConditions.elementToBeClickable(uploadImage));
            uploadImage.click();

            // Disable to avoid captcha error
            /*wait.until(ExpectedConditions.elementToBeClickable(choseFileButton));
            choseFileButton.sendKeys(filePath);*/
        }else{
            Assert.fail("Image File not found, imagePath: " + filePath);
        }
    }

    public void validateCaptchaError(){
        wait.until(ExpectedConditions.elementToBeClickable(captchaError));
        Assert.assertEquals(captchaError.getText(), MessageConstants.CAPTCHA_ERROR,
                "Asserting if Captcha Error is displayed");
    }

    public void validateImageResults(){
        wait.until(ExpectedConditions.elementToBeClickable(captchaError));
        Assert.assertEquals(captchaError.getText(), MessageConstants.CAPTCHA_ERROR,
                "Asserting if Captcha Error is displayed");
    }

    public boolean isCaptchaPresent(){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(captchaError));
            return true;
        }catch(Exception e) {
            //e.printStackTrace();
            return false;
        }
    }
}
