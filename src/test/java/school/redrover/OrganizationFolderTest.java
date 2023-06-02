package school.redrover;

import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.model.MainPage;
import school.redrover.runner.BaseTest;

public class OrganizationFolderTest extends BaseTest {

    @Test
    public void testCreateOrganizationFolder() {
        final String expectedNewFolderName = "Project1";

        String actualNewFolderName = new MainPage(getDriver())
                .clickNewItem()
                .enterItemName(expectedNewFolderName)
                .selectOrganizationFolder()
                .clickOkButton()
                .clickSaveButton()
                .getHeader()
                .clickLogo()
                .getProjectNameMainPage(expectedNewFolderName);

        Assert.assertEquals(actualNewFolderName, expectedNewFolderName);
    }

    @Test(dependsOnMethods = "testCreateOrganizationFolder")
    public void testRenameOrganizationFolder() {
        final String originalNewFolderName = "Project1";
        final String expectedRenamedFolderName = "Project";

        String actualRenamedFolderName = new MainPage(getDriver())
                .clickMultiConfigurationProject(originalNewFolderName)
                .clickRename()
                .enterNewName(expectedRenamedFolderName)
                .submitNewName()
                .getMultiProjectName()
                .getText();

        Assert.assertEquals(actualRenamedFolderName, expectedRenamedFolderName);
    }

    @Test
    public void testMoveOrganizationFolderToFolderFromOrganizationFolderPage() {

        final String folderName = "TestFolder";
        final String organizationFolderName = "TestOrgFolder";

        boolean movedOrgFolderVisibleAndClickable = new MainPage(getDriver())
                .clickNewItem()
                .enterItemName(folderName)
                .selectFolder()
                .clickOkButton()
                .clickSaveButton()
                .clickNewItem()
                .enterItemName(organizationFolderName)
                .selectOrganizationFolder()
                .clickOkButton()
                .clickSaveButton()
                .clickMoveOnLeftMenu()
                .selectDestinationFolder(folderName)
                .clickMoveButton()
                .clickDashboard()
                .clickFolderName(folderName)
                .nestedFolderIsVisibleAndClickable(organizationFolderName);

        Assert.assertTrue(movedOrgFolderVisibleAndClickable);
    }
}
