package pageobjects.democart;

import constants.MessageConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import pageobjects.BasePage;
import utils.TestDataFactory;

import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends BasePage {

    private static final Logger LOGGER = LogManager.getLogger(HomePage.class);

    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "//*[@class='nav navbar-nav']/li/a")
    List<WebElement> menuBarElements;

    @FindBy(xpath = "//*[@class='col-sm-4']/div/h1/a")
    WebElement headerLabel;

    @FindBy(name = "search")
    WebElement searchBox;

    @FindBy(className = "input-group-btn")
    WebElement searchButton;

    @SuppressWarnings("unchecked")
    public void validateMenuBarElements(){
        List<String> menuOptionsExpected = (List<String>) TestDataFactory.getTestData("listItem","menuOptions");
        wait.until(ExpectedConditions.visibilityOfAllElements(menuBarElements));
        List<String> menuOptionsActual = menuBarElements.stream().map(e -> {
                                            try {
                                                return e.getText();
                                            } catch(Throwable t) {
                                                System.out.println(t.getMessage());
                                                return "";
                                            }
                                        }).collect(Collectors.toList());
        Assert.assertEquals(menuOptionsActual, menuOptionsExpected, "Asserting menuOptions are displayed as expected");
        LOGGER.info("MenuOptions are displayed as expected");
    }

    public void validateHomePageLabel(){
        wait.until(ExpectedConditions.elementToBeClickable(headerLabel));
        Assert.assertEquals(headerLabel.getAttribute("href"),
                MessageConstants.HOME_LABEL_URL,
                "Asserting Home Page Label href URL");
        Assert.assertEquals(headerLabel.getText(),
                MessageConstants.HOME_LABEL_TEXT,
                "Asserting Home Page Label Text");
    }

    public void searchByButton(String searchTerm){
        wait.until(ExpectedConditions.visibilityOf(searchBox));
        searchBox.sendKeys(searchTerm);
        //Assert.assertEquals(searchBox.getText(), searchTerm, "Asserting if text is entered");
        wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        searchButton.click();
    }

    public void searchByEnterKey(String searchTerm){
        wait.until(ExpectedConditions.visibilityOf(searchBox));
        searchBox.sendKeys(searchTerm);
        Assert.assertEquals(searchBox.getText(), searchTerm, "Asserting if text is entered");
        searchBox.sendKeys(Keys.ENTER);
    }

}
