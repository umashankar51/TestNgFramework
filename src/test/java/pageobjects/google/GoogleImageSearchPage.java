package pageobjects.google;

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

public class GoogleImageSearchPage extends BasePage {

    private static final Logger LOGGER = LogManager.getLogger(GoogleImageSearchPage.class);

    public GoogleImageSearchPage(WebDriver driver) {
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
            wait.until(ExpectedConditions.elementToBeClickable(choseFileButton));
            choseFileButton.sendKeys(filePath);
        }else{
            Assert.fail("Image File not found, imagePath: " + filePath);
        }
    }
}
