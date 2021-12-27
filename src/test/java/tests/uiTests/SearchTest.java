package tests.uiTests;

import dto.ItemDTO;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageobjects.democart.HomePage;
import pageobjects.democart.SearchResultsPage;
import tests.baseTests.BaseUITest;
import utils.ConfigUtils;
import utils.TestDataFactory;

import java.util.List;

public class SearchTest extends BaseUITest {

    private HomePage homePage;
    private SearchResultsPage searchResultsPage;

    @BeforeMethod(alwaysRun = true)
    public void beforeChildMethod(){
        homePage = new HomePage(driver);
        searchResultsPage = new SearchResultsPage(driver);
    }

    @Test(groups= {"searchTest","regression","uiTests"})
    public void tc_p1_searchTestByButton_test() {
        ItemDTO itemDTO = (ItemDTO) TestDataFactory.getTestData("searchItem", "item1");
        Assert.assertNotNull(itemDTO);
        driver.get(ConfigUtils.getEnvVariable("UI_HOME"));
        homePage.searchByButton(itemDTO.getName());
        List<ItemDTO> searchResultsList = searchResultsPage.getSearchResultList();
        Assert.assertTrue(searchResultsList.get(0).equals(itemDTO),
                "Asserting search results");
    }

    @Test(groups= {"searchTest","regression","uiTests"})
    public void tc_p1_searchTestByEnterKey_test() {
        ItemDTO itemDTO = (ItemDTO) TestDataFactory.getTestData("searchItem", "item1");
        Assert.assertNotNull(itemDTO);
        driver.get(ConfigUtils.getEnvVariable("UI_HOME"));
        homePage.searchByEnterKey(itemDTO.getName());
        List<ItemDTO> searchResultsList = searchResultsPage.getSearchResultList();
        Assert.assertTrue(searchResultsList.get(0).equals(itemDTO),
                "Asserting search results");
    }

    @Test(groups= {"noResultFoundTest","regression","uiTests"})
    public void tc_p1_searchNoResultTestByButton_test() {
        ItemDTO itemDTO = (ItemDTO) TestDataFactory.getTestData("searchItem", "noSearchItem");
        Assert.assertNotNull(itemDTO);
        driver.get(ConfigUtils.getEnvVariable("UI_HOME"));
        homePage.searchByButton(itemDTO.getName());
        searchResultsPage.validateNoSearchMessage();
    }
}
