package pageobjects.ios;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import pageobjects.BasePage;

public class LoginPage extends BasePage {

    @iOSXCUITFindBy(accessibility = "test-Username")
    private WebElement usernameField;

    @iOSXCUITFindBy(accessibility = "test-Password")
    private WebElement passwordField;

    @iOSXCUITFindBy(accessibility = "test-LOGIN")
    private WebElement loginButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name='test-Error message']/XCUIElementTypeStaticText")
    private WebElement errorMessage;

    public LoginPage(AppiumDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        typeText(usernameField, username);
        typeText(passwordField, password);
        tapElement(loginButton);
    }

    public void enterUsername(String username) {
        typeText(usernameField, username);
    }

    public void enterPassword(String password) {
        typeText(passwordField, password);
    }

    public void tapLogin() {
        tapElement(loginButton);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed(loginButton);
    }
}
