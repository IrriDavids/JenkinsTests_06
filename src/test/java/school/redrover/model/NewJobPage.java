package school.redrover.model;

import org.checkerframework.checker.units.qual.C;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import school.redrover.model.base.BaseConfigPage;
import school.redrover.model.base.BaseMainHeaderPage;
import school.redrover.model.base.BaseModel;

public class NewJobPage<ConfigPage extends BaseConfigPage<?,?>> extends BaseMainHeaderPage<NewJobPage<?>> {

    private final ConfigPage configPage;

    public NewJobPage(WebDriver driver, ConfigPage configPage) {
        super(driver);
        this.configPage = configPage;
    }

    public NewJobPage<ConfigPage> enterItemName(String nameJob) {
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='name']"))).sendKeys(nameJob);
        return this;
    }

    public FreestyleProjectConfigPage selectFreestyleProjectAndOk() {
        getFreestyleProject().click();
        getOkButton().click();
        return new FreestyleProjectConfigPage(new FreestyleProjectPage(getDriver()));
    }

    public PipelineConfigPage selectPipelineAndOk() {
        getDriver().findElement(By.xpath("//span[text()='Pipeline']")).click();
        getOkButton().click();
        return new PipelineConfigPage(new PipelinePage(getDriver()));
    }

    public MultiConfigurationProjectConfigPage selectMultiConfigurationProjectAndOk() {
        getDriver().findElement(By.xpath("//span[.='Multi-configuration project']")).click();
        getOkButton().click();
        return new MultiConfigurationProjectConfigPage(new MultiConfigurationProjectPage(getDriver()));
    }

    public NewJobPage<ConfigPage> selectMultiConfigurationProject() {
        getWait2().until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@class='hudson_matrix_MatrixProject']"))).click();
        return this;
    }

    public FolderConfigPage selectFolderAndOk() {
        getDriver().findElement(By.xpath("//li[contains(@class, 'folder_Folder')]")).click();
        getOkButton().click();
        return new FolderConfigPage(new FolderPage(getDriver()));
    }

    public MultibranchPipelineConfigPage selectMultibranchPipelineAndOk() {
        getDriver().findElement(By.xpath("//li[contains(@class, 'WorkflowMultiBranchProject')]")).click();
        getOkButton().click();
        return new MultibranchPipelineConfigPage(new MultibranchPipelinePage(getDriver()));
    }

    public NewJobPage<OrganizationFolderConfigPage> selectOrganizationFolder() {
        getDriver().findElement(By.xpath("//li[contains(@class, 'OrganizationFolder')]")).click();
        return new NewJobPage<>(getDriver(), new OrganizationFolderConfigPage(new OrganizationFolderPage(getDriver())));
    }

    public NewJobPage<ConfigPage>copyFrom(String typeToAutocomplete) {
        getDriver().findElement(By.xpath("//input[contains(@autocompleteurl, 'autoCompleteCopyNewItemFrom')]"))
                .sendKeys(typeToAutocomplete);
        return this;
    }

    public String getItemInvalidMessage() {
        return getWait2().until(ExpectedConditions.visibilityOf(getItemInvalidNameMessage())).getText();
    }

    public NewJobPage<ConfigPage> selectFreestyleProject() {
        getWait5().until(ExpectedConditions.elementToBeClickable(getFreestyleProject())).click();
        return this;
    }

    public boolean isOkButtonEnabled() {
        return getOkButton().isEnabled();
    }

    public CreateItemErrorPage clickOkToCreateWithExistingName() {
        getOkButton().click();
        return new CreateItemErrorPage(getDriver());
    }

    public String getItemNameRequiredMessage() {
        return getDriver().findElement(By.id("itemname-required")).getText();
    }

    public WebElement getOkButton() {
        return getDriver().findElement(By.xpath("//button[@id='ok-button']"));
    }

    private WebElement getFreestyleProject() {
        return getDriver().findElement(By.className("hudson_model_FreeStyleProject"));
    }

    private WebElement getItemInvalidNameMessage() {
        return getDriver().findElement(By.id("itemname-invalid"));
    }

    public NewJobPage<ConfigPage> selectPipelineProject() {
        getWait2().until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Pipeline']"))).click();
        return this;
    }

    public String getItemNameRequiredErrorText() {
        return getWait2().until(ExpectedConditions.visibilityOfElementLocated(By.id("itemname-required"))).getText();
    }
    public NewJobPage<ConfigPage> clickButtonOk() {
        getWait2().until(ExpectedConditions.elementToBeClickable(By.id("ok-button")))
                .click();
        return this;
    }

    public ConfigPage clickOkButton() {
        getWait2().until(ExpectedConditions.elementToBeClickable(By.id("ok-button"))).click();
        return configPage;
    }

    public FolderConfigPage copyFromFolder(String typeToAutocomplete) {
        getDriver().findElement(By.id("from"))
                .sendKeys(typeToAutocomplete);
        clickButtonOk();
        return new FolderConfigPage(new FolderPage(getDriver()));
    }
}
