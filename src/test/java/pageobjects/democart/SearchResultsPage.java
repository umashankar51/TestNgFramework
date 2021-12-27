package pageobjects.democart;

import constants.MessageConstants;
import dto.ItemDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import pageobjects.BasePage;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsPage extends BasePage {

    public SearchResultsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "//*[@for='input-search']")
    WebElement searchCriteriaLabel;

    @FindBy(xpath = "//*[@class='product-thumb']")
    List<WebElement> searchResults;

    @FindBy(xpath = "(//*[*[@id='button-search']]/p)[2]")
    WebElement noResultsMessage;

    public List<ItemDTO> getSearchResultList(){
        wait.until(ExpectedConditions.visibilityOf(searchCriteriaLabel));
        Assert.assertEquals(searchCriteriaLabel.getText(),
                MessageConstants.SEARCH_CRITERIA_LABEL_TEXT);
        wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));

        List<ItemDTO> resultsDTO = new ArrayList<>();
        for(WebElement searchResult: searchResults){
            ItemDTO itemDTO = new ItemDTO();
            wait.until(ExpectedConditions.elementToBeClickable(
                    searchResult.findElement(By.xpath(".//button[*[@class='fa fa-shopping-cart']]"))));
            wait.until(ExpectedConditions.elementToBeClickable(
                    searchResult.findElement(By.xpath(".//button[*[@class='fa fa-heart']]"))));
            wait.until(ExpectedConditions.elementToBeClickable(
                    searchResult.findElement(By.xpath(".//button[*[@class='fa fa-exchange']]"))));
            itemDTO.setName(searchResult.findElement(By.xpath(".//h4")).getText());
            itemDTO.setDescription(searchResult.findElement(By.xpath(".//*[@class='caption']/p[1]")).getText());
            itemDTO.setPrice(searchResult.findElement(By.xpath(".//*[@class='price']")).getText().trim().split("\n")[0]);
            itemDTO.setTax(searchResult.findElement(By.xpath(".//*[@class='price']/span")).getText().trim());
            resultsDTO.add(itemDTO);
        }
        return resultsDTO;
    }

    public void validateNoSearchMessage() {
        wait.until(ExpectedConditions.visibilityOf(noResultsMessage));
        Assert.assertEquals(noResultsMessage.getText(), MessageConstants.NO_RESULTS_MESSAGE,
                "Asserting search results");
    }
}
