package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import school.redrover.model.MainPage;
import school.redrover.model.ManageJenkinsPage;
import school.redrover.runner.BaseTest;

import java.util.List;
import java.util.Objects;

import static school.redrover.runner.TestUtils.getRandomStr;

public class ManageJenkinsTest extends BaseTest {

    final String NAME_NEW_NODE = "testNameNewNode";

    public boolean isTitleAppeared(List<WebElement> titleTexts, String title) {
        for (WebElement element : titleTexts) {
            if (element.getText().equals(title)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testSearchWithLetterConfigureSystem() {
        String configurePage = new MainPage(getDriver())
                .navigateToManageJenkinsPage()
                .inputToSearchField("m")
                .selectOnTheFirstLineInDropdown()
                .getConfigureSystemPage();

        Assert.assertEquals(configurePage, "Configure System");
    }

    @Test
    public void testNavigateToManageJenkinsFromMainPageUsingDashboard() {

        String page = new MainPage(getDriver())
                .clickManageJenkinsOnDropDown()
                .verifyManageJenkinsPage();

        Assert.assertEquals(page, "Manage Jenkins");
    }

    @Test
    public void testNameNewNodeOnCreatePage() {
        final String nodeName = "NodeTest";
        String actualNodeName = new MainPage(getDriver())
                .clickBuildExecutorStatus()
                .clickNewNodeButton()
                .inputNodeNameField(nodeName)
                .clickPermanentAgentRadioButton()
                .clickCreateButton()
                .clickSaveButton()
                .getNodeName(nodeName);
        Assert.assertEquals(actualNodeName, nodeName);
    }

    @Test
    public void testTextErrorWhenCreateNewNodeWithEmptyName() {

        String textError = new MainPage(getDriver())
                .navigateToManageJenkinsPage()
                .clickManageNodes()
                .clickNewNodeButton()
                .inputNodeNameField(NAME_NEW_NODE)
                .clickPermanentAgentRadioButton()
                .clickCreateButton()
                .clearNameField()
                .clickSaveButtonWhenNameFieldEmpty()
                .getTextError();

        Assert.assertEquals(textError, "Query parameter 'name' is required");
    }

    @Test
    public void testSearchNumericSymbol() {

        String searchText = new MainPage(getDriver())
                .navigateToManageJenkinsPage()
                .inputToSearchField("1")
                .getNoResultTextInSearchField();

        Assert.assertEquals(searchText, "No results");
    }

    @Test
    public void testSearchConfigureSystemByC() {
        String oldUrl = getDriver().getCurrentUrl();

        getDriver().findElement(By.xpath("//a[@href='/manage']")).click();
        getDriver().findElement(By.id("settings-search-bar")).sendKeys("c");

        getWait10().until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//div[contains(@class, 'results-container')]")));

        List<WebElement> titleTexts = getDriver()
                .findElements(By.xpath("//div/a[contains(@href, 'manage')]"));

        Assert.assertTrue(isTitleAppeared(titleTexts, "Configure System"));

        getWait2()
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='jenkins-search__results-item--selected']")))
                .click();
        getWait10().until(t -> !Objects.equals(getDriver().getCurrentUrl(), oldUrl));

        Assert.assertEquals(getDriver().getTitle(), "Configure System [Jenkins]");
    }

    @DataProvider(name = "keywords")
    public Object[][] searchSettingsItem() {
        return new Object[][]{{"manage"}, {"tool"}, {"sys"}, {"sec"}, {"cred"}, {"dow"}, {"script"}, {"jenkins"}, {"stat"}};
    }

    @Test(dataProvider = "keywords")
    public void testSearchSettingsItemsByKeyword(String keyword) {

        boolean manageJenkinsPage = new MainPage(getDriver())
                .navigateToManageJenkinsPage()
                .inputToSearchField(keyword)
                .selectAllDropdownResultsFromSearchField()
                .isDropdownResultsFromSearchFieldContainsTextToSearch(keyword);

        Assert.assertTrue(manageJenkinsPage);
    }

    @DataProvider(name = "ToolsAndActions")
    public Object[][] searchToolsAndActions() {
        return new Object[][]{{"Script Console"}, {"Jenkins CLI"}, {"Prepare for Shutdown"}};
    }

    @Test(dataProvider = "ToolsAndActions")
    public void testSearchToolsAndActions(String inputText) {
        String searchResult = new MainPage(getDriver())
                .navigateToManageJenkinsPage()
                .inputToSearchField(inputText)
                .getDropdownResultsInSearchField();
        Assert.assertEquals(searchResult, inputText);
    }

    @Test
    public void testAccessSearchSettingsFieldUsingShortcutKey() {
        final String partOfSettingsName = "manage";

        ManageJenkinsPage manageJenkinsPage = new MainPage(getDriver())
                .navigateToManageJenkinsPage()
                .inputToSearchFieldUsingKeyboardShortcut(partOfSettingsName)
                .selectAllDropdownResultsFromSearchField();

        Assert.assertTrue(manageJenkinsPage.isDropdownResultsFromSearchFieldContainsTextToSearch(partOfSettingsName));
        Assert.assertTrue(manageJenkinsPage.isDropdownResultsFromSearchFieldLinks());
    }

    @Test
    public void testCreateNewAgentNode() {
        final String nodeName = getRandomStr(10);

        String manageNodesPage = new MainPage(getDriver())
                .navigateToManageJenkinsPage()
                .clickManageNodes()
                .clickNewNodeButton()
                .inputNodeNameField(nodeName)
                .clickPermanentAgentRadioButton()
                .clickCreateButton()
                .clickSaveButton()
                .getNodeName(nodeName);

        Assert.assertEquals(manageNodesPage, nodeName);
    }

    @Test
    public void testCreateNewAgentNodeWithDescription() {
        final String nodeName = getRandomStr(10);
        final String description = getRandomStr(50);

        String nodeDescription = new MainPage(getDriver())
                .navigateToManageJenkinsPage()
                .clickManageNodes()
                .clickNewNodeButton()
                .inputNodeNameField(nodeName)
                .clickPermanentAgentRadioButton()
                .clickCreateButton()
                .addDescription(description)
                .clickSaveButton()
                .clickOnNode(nodeName)
                .getNodeDescription();

        Assert.assertEquals(nodeDescription, description);

    }

    @Test
    public void testCreateNewAgentNodeByCopyingExistingNode() {
        final String nodeName = getRandomStr(10);
        final String newNodeName = getRandomStr(10);
        final String description = getRandomStr(50);

        String newNodeDescription = new MainPage(getDriver())
                .navigateToManageJenkinsPage()
                .clickManageNodes()
                .clickNewNodeButton()
                .inputNodeNameField(nodeName)
                .clickPermanentAgentRadioButton()
                .clickCreateButton()
                .addDescription(description)
                .clickSaveButton()
                .clickNewNodeButton()
                .inputNodeNameField(newNodeName)
                .clickCopyExistingNode()
                .inputExistingNode(nodeName)
                .clickCreateButton()
                .clickSaveButton()
                .clickOnNode(newNodeName)
                .getNodeDescription();

        Assert.assertEquals(newNodeDescription, description);
    }

    @Test
    public void testCreateNewAgentNodeByCopyingNonExistingNode() {
        final String nonExistingNodeName = ".0";

        String errorMessage = new MainPage(getDriver())
                .navigateToManageJenkinsPage()
                .clickManageNodes()
                .clickNewNodeButton()
                .inputNodeNameField("NewNode")
                .clickCopyExistingNode()
                .inputExistingNode(nonExistingNodeName)
                .clickCreateButtonAndGoError()
                .getErrorMessage();

        Assert.assertEquals(errorMessage, "No such agent: " + nonExistingNodeName);
    }
}
