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

public class GoogleImageResultsPage extends BasePage {

    private static final Logger LOGGER = LogManager.getLogger(GoogleImageResultsPage.class);

    public GoogleImageResultsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver,this);
    }

    @FindBy (className = "O1id0e")
    public WebElement googleImagesResultOptions;

    public void validateImageResults(){
        wait.until(ExpectedConditions.visibilityOf(googleImagesResultOptions));
        Assert.assertEquals(googleImagesResultOptions.getText(), MessageConstants.OTHER_IMAGE_SIZE_MESSAGE,
                "Asserting if Other Image Sizes are displayed");
        LOGGER.info("Other images sizes displayed");
    }
}
