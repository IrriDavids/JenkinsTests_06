package school.redrover.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import school.redrover.model.base.BaseMainHeaderPage;
import school.redrover.runner.TestUtils;

import java.util.ArrayList;
import java.util.List;

public class MyViewsPage extends BaseMainHeaderPage<MyViewsPage> {

    public MyViewsPage(WebDriver driver) {
        super(driver);
    }

    public MyViewsPage clickOkButton() {
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#ok-button"))).click();
        return this;
    }

    public MyViewsPage clickOnDescription() {
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.id("description-link"))).click();
        return new MyViewsPage(getDriver());
    }

    public MyViewsPage enterDescription (String name){
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//textarea[@name='description']"))).sendKeys(name);
        return new MyViewsPage(getDriver());
    }

    public MyViewsPage clickSaveButtonDescription(){
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='jenkins-button jenkins-button--primary ']"))).click();
        return new MyViewsPage(getDriver());
    }

    public String getTextFromDescription(){

        return getWait5().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='description']/div[not(@class)]"))).getText();
    }

    public MyViewsPage clearTextFromDescription() {
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//textarea[@name='description']"))).clear();
        return new MyViewsPage(getDriver());
    }

    public MyViewsPage enterNewDescription (String name){
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//textarea[@name='description']"))).sendKeys(name);
        return new MyViewsPage(getDriver());
    }

    public NewJobPage clickNewItem() {
        getDriver().findElement(By.cssSelector(".task-link-wrapper>a[href$='newJob']")).click();
        return new NewJobPage(getDriver());
    }

    public String getStatusMessageText() {

        return getDriver().findElement(By.xpath("//h2")).getText();
    }

    public NewViewPage clickNewViewButton() {
        TestUtils.click(this, getDriver().findElement(By.xpath("//a[@class='addTab']")));

        return new NewViewPage(getDriver());
    }

    public String getActiveView() {

        return TestUtils.getText(this, getDriver().findElement(By.xpath("//div[@class = 'tab active']")));
    }

    public MyViewsPage clickInactiveLastCreatedMyView() {
        TestUtils.click(this, getDriver().findElement(By.xpath("//div[@class = 'tab'][last()-1]")));

        return this;
    }

    public MyViewsPage editMyViewNameAndClickSubmitButton(String editedMyViewName) {
        TestUtils.click(this, getDriver().findElement(By.xpath("//a[contains(@href, '/configure')]")));
        TestUtils.sendTextToInput(this, getDriver().findElement(By.xpath("//input[@name = 'name']")), editedMyViewName);
        TestUtils.click(this, getDriver().findElement(By.xpath("//button[@name = 'Submit']")));

        return this;
    }

    public DeletePage<MyViewsPage> clickDeleteViewButton() {
        TestUtils.click(this, getDriver().findElement(By.xpath("//a[@href = 'delete']")));

        return new DeletePage<>(getDriver(), this);
    }

    public List<String> getListOfAllViews() {
        List<String> list = new ArrayList<>();
        List<WebElement> views = getDriver().findElements(By.xpath("//div[@class='tabBar']//div[starts-with(@class, 'tab')]"));
        for (WebElement view : views) {
            list.add(view.getText());
        }

        return list;
    }

    public NewJobPage clickCreateAJobArrow() {
        getWait2().until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='newJob']/span[@class = 'trailing-icon']"))).click();

        return new NewJobPage(getDriver());
    }
}
