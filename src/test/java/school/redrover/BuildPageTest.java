package school.redrover;

import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import school.redrover.model.*;
import school.redrover.runner.BaseTest;
import school.redrover.runner.TestUtils;

public class BuildPageTest extends BaseTest {

    private static final String NAME_PIPELINE = "Pipeline2023";
    private static final String BUILD_DESCRIPTION = "For QA";
    private final String FREESTYLE_PROJECT_NAME = "FreestyleName";
    private final String MULTI_CONFIGURATION_PROJECT_NAME = "MultiConfiguration001";

    @Test
    public void testBuildHistoryOfTwoDifferentTypesProjectsIsShown() {
        TestUtils.createMultiConfigurationProject(this, MULTI_CONFIGURATION_PROJECT_NAME, true);
        TestUtils.createFreestyleProject(this, FREESTYLE_PROJECT_NAME,true);

        int numberOfLinesInBuildHistoryTable = new MainPage(getDriver())
                .getHeader()
                .clickLogo()
                .clickJobDropdownMenuBuildNow(MULTI_CONFIGURATION_PROJECT_NAME)
                .clickJobDropdownMenuBuildNow(FREESTYLE_PROJECT_NAME)
                .clickBuildsHistoryButton()
                .getNumberOfLinesInBuildHistoryTable();

        Assert.assertTrue(numberOfLinesInBuildHistoryTable >= 2);
    }

    @Test
    public void testAddDescriptionToBuild() {
        String buildDescription = new MainPage(getDriver())
                .clickNewItem()
                .enterItemName(NAME_PIPELINE)
                .selectJobType(TestUtils.JobType.Pipeline)
                .clickOkButton(new PipelineConfigPage(new PipelinePage(getDriver())))
                .clickSaveButton()
                .getHeader()
                .clickLogo()
                .clickJobName(NAME_PIPELINE, new PipelinePage(getDriver()))
                .clickEditDescription()
                .enterDescription(BUILD_DESCRIPTION)
                .clickSaveButton()
                .getDescription();

        Assert.assertEquals(buildDescription, BUILD_DESCRIPTION);
    }

    @Ignore
    @Test
    public void testConsoleFreestyleBuildLocation() {
        String consoleOutputText = new MainPage(getDriver())
                .clickNewItem()
                .enterItemName(FREESTYLE_PROJECT_NAME)
                .selectJobType(TestUtils.JobType.FreestyleProject)
                .clickOkButton(new FreestyleProjectConfigPage(new FreestyleProjectPage(getDriver())))
                .clickSaveButton()
                .selectBuildNow()
                .getHeader()
                .clickLogo()
                .clickBuildsHistoryButton()
                .clickProjectBuildConsole(FREESTYLE_PROJECT_NAME)
                .getConsoleOutputText();

        String actualLocation = new ConsoleOutputPage(getDriver())
                .getParameterFromConsoleOutput(consoleOutputText, "workspace");

        Assert.assertEquals(actualLocation, "Building in workspace /var/jenkins_home/workspace/" + FREESTYLE_PROJECT_NAME);
    }

    @Test
    public void testConsoleOutputFreestyleBuildStartedByUser() {
        final String currentUser = new MainPage(getDriver()).getHeader().getCurrentUserName();

        final String userConsoleOutput = new MainPage(getDriver())
                .clickNewItem()
                .enterItemName(FREESTYLE_PROJECT_NAME)
                .selectJobType(TestUtils.JobType.FreestyleProject)
                .clickOkButton(new FreestyleProjectConfigPage(new FreestyleProjectPage(getDriver())))
                .clickSaveButton()
                .selectBuildNow()
                .getHeader()
                .clickLogo()
                .clickBuildsHistoryButton()
                .clickProjectBuildConsole(FREESTYLE_PROJECT_NAME)
                .getStartedByUser();

        Assert.assertEquals(currentUser, userConsoleOutput);
    }

    @Test
    public void testConsoleOutputFreestyleBuildStatus(){
        final String consoleOutput = new MainPage(getDriver())
                .clickNewItem()
                .enterItemName(FREESTYLE_PROJECT_NAME)
                .selectJobType(TestUtils.JobType.FreestyleProject)
                .clickOkButton(new FreestyleProjectConfigPage(new FreestyleProjectPage(getDriver())))
                .clickSaveButton()
                .selectBuildNow()
                .getHeader()
                .clickLogo()
                .clickBuildsHistoryButton()
                .clickProjectBuildConsole(FREESTYLE_PROJECT_NAME)
                .getConsoleOutputText();

        String actualStatus = new ConsoleOutputPage(getDriver())
                .getParameterFromConsoleOutput(consoleOutput, "Finished");

        Assert.assertEquals(actualStatus, "Finished: SUCCESS");
    }

    @Ignore
    @Test
    public void verifyStatusBroken(){

        final String namePipeline = "NewBuilds";
        final String textToDescriptionField = "What's up";
        final String textToPipelineScript = "Test";
        final String expectedStatusMessageText = "broken since this build";

        String actualStatusMessageText = new MainPage(getDriver())
                .clickNewItem()
                .enterItemName(namePipeline)
                .selectJobType(TestUtils.JobType.Pipeline)
                .clickOkButton(new PipelineConfigPage(new PipelinePage(getDriver())))
                .addDescription(textToDescriptionField)
                .scrollToBuildTriggers()
                .clickBuildTriggerCheckBox()
                .scrollToPipelineSection()
                .sendAreContentInputString(textToPipelineScript)
                .clickSaveButton()
                .getHeader()
                .clickLogo()
                .clickPlayBuildForATestButton("NewBuilds")
                .clickBuildsHistoryButton()
                .getStatusMessageText();
        Assert.assertEquals(actualStatusMessageText,expectedStatusMessageText);
    }

    @Test
    public void testPresenceProjectNameOnBuildHistoryTimeline() {
        final String itemName = "TestProject";
        String projectNameOnBuildHistoryTimeline = new MainPage(getDriver())
                .clickNewItem()
                .enterItemName(itemName)
                .selectJobType(TestUtils.JobType.FreestyleProject)
                .clickOkButton(new FreestyleProjectConfigPage(new FreestyleProjectPage(getDriver())))
                .clickSaveButton()
                .getHeader()
                .clickLogo()
                .clickPlayBuildForATestButton(itemName)
                .clickBuildsHistoryButton()
                .clickBuildNameOnTimeline(itemName + " #1")
                .getBubbleTitleOnTimeline();
        Assert.assertEquals(projectNameOnBuildHistoryTimeline, itemName + " #1");
    }
}
