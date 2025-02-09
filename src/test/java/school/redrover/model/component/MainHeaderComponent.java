package school.redrover.model.component;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import school.redrover.model.*;
import school.redrover.model.base.BaseComponent;
import school.redrover.model.base.BasePage;
import school.redrover.runner.TestUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MainHeaderComponent<Page extends BasePage<?, ?>> extends BaseComponent<Page> {

    public MainHeaderComponent(Page page) {
        super(page);
    }

    private static final By NOTIFICATION_ICON = By.id("visible-am-button");
    private static final By ADMIN_BUTTON = By.xpath("//a[@href='/user/admin']");
    private static final By LOGOUT_BUTTON = By.xpath("//a[@href='/logout']");

    private void hoverOver(By locator) {
        new Actions(getDriver())
                .moveToElement(getDriver().findElement(locator))
                .pause(Duration.ofMillis(300))
                .perform();
    }

    public MainHeaderComponent<Page> clickDashboardDropdownMenu() {
        hoverOver(By.xpath("//a[text()='Dashboard']"));
        getDriver().findElement(By.xpath("//a[text()='Dashboard']/button")).sendKeys(Keys.RETURN);
        return this;
    }

    private void clickDashboardDropdownMenu(By subMenu) {
        clickDashboardDropdownMenu();
        new Actions(getDriver())
                .moveToElement(getDriver().findElement(By.xpath("//a[@class='yuimenuitemlabel yuimenuitemlabel-hassubmenu']")))
                .pause(Duration.ofSeconds(1))
                .moveToElement(getDriver().findElement(subMenu))
                .click()
                .perform();
    }

    private String getIconBackgroundColor(By locator) {
        return getDriver().findElement(locator).getCssValue("background-color");
    }

    public MainPage clickLogo() {
        getWait2().until(ExpectedConditions.visibilityOfElementLocated(By.id("jenkins-head-icon"))).click();
        return new MainPage(getDriver());
    }

    public MainHeaderComponent<Page> clickNotificationIcon() {
        getWait2().until(ExpectedConditions.elementToBeClickable(NOTIFICATION_ICON)).click();
        return this;
    }

    public MainHeaderComponent<Page> clickAdminDropdownMenu() {
        TestUtils.clickByJavaScript(this, getDriver().findElement(By.xpath("//a[@href='/user/admin']/button")));
        return this;
    }

    public boolean isPopUpNotificationScreenDisplayed() {
        return getWait2().until(ExpectedConditions.visibilityOfElementLocated(By.id("visible-am-list"))).isDisplayed();
    }

    public boolean isAdminDropdownScreenDisplayed() {
        return getWait2().until(ExpectedConditions.visibilityOfElementLocated(By.id("breadcrumb-menu"))).isDisplayed();
    }

    public MainHeaderComponent<Page> hoverOverNotificationIcon() {
        hoverOver(NOTIFICATION_ICON);
        return this;
    }

    public MainHeaderComponent<Page> hoverOverAdminButton() {
        hoverOver(ADMIN_BUTTON);
        return this;
    }

    public MainHeaderComponent<Page> hoverOverLogOutButton() {
        hoverOver(LOGOUT_BUTTON);
        return this;
    }

    public String getNotificationIconBackgroundColor() {
        return getIconBackgroundColor(NOTIFICATION_ICON);
    }

    public String getAdminButtonBackgroundColor() {
        return getIconBackgroundColor(ADMIN_BUTTON);
    }

    public String getLogOutButtonBackgroundColor() {
        return getIconBackgroundColor(LOGOUT_BUTTON);
    }

    public String getAdminTextDecorationValue() {
        WebElement adminLink = getWait5().until(ExpectedConditions.visibilityOfElementLocated(ADMIN_BUTTON));
        return adminLink.getCssValue("text-decoration");
    }

    public WebElement openBuildsTabFromAdminDropdownMenu() {
        getWait5().until(ExpectedConditions.elementToBeClickable(By.xpath
                ("//div[@id='breadcrumb-menu']//span[.='Builds']"))).click();
        return getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.xpath
                ("//h1[.='Builds for admin']")));
    }

    public WebElement openConfigureTabFromAdminDropdownMenu () {
        getWait5().until(ExpectedConditions.elementToBeClickable(By.xpath
                ("//span[. ='Configure']"))).click();
        return getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.xpath
                ("//li[@class='jenkins-breadcrumbs__list-item'][3]")));
    }

    public WebElement openMyViewsTabFromAdminDropdownMenu () {
        getWait5().until(ExpectedConditions.elementToBeClickable(By.xpath
                ("//div[@class='bd']//span[.='My Views']"))).click();
        return getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.xpath
                ("//a[@href='/user/admin/my-views/']")));
    }

    public WebElement openCredentialsTabFromAdminDropdownMenu () {
        getWait5().until(ExpectedConditions.elementToBeClickable(By.xpath
                ("//span[.='Credentials']"))).click();
        return getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.xpath
                ("//h1[.='Credentials']")));
    }

    public String getCurrentUserName() {
        return getDriver().findElement(By.xpath("//a[@class='model-link']/span[contains(@class,'hidden-xs')]")).getAttribute("innerText");
    }

    public String getBackgroundColorNotificationIcon() {
        return getDriver().findElement(By.id("visible-am-button")).getCssValue("background-color");
    }

    public String getLinkVersion() {
        return getDriver().findElement(By.xpath("//a[text()='Jenkins 2.387.2']")).getText();
    }

    public NewJobPage clickNewItemDashboardDropdownMenu(){
        clickDashboardDropdownMenu();
        getDriver().findElement(By.xpath("//div[@id='breadcrumb-menu']/div/ul/li/a")).click();
        return new NewJobPage(getDriver());
    }

    public PluginsPage openPluginsPageFromDashboardDropdownMenu () {
        clickDashboardDropdownMenu(By.xpath("//span[contains(text(), 'Manage Plugins')]"));
        return new PluginsPage(getDriver());
    }

    public MainPage clickDashboardButton() {
        getWait2().until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("//a[contains(text(), 'Dashboard')]"))).click();

        return new MainPage(getDriver());
    }

    public LoginPage clickLogoutButton() {
        getDriver().findElement(LOGOUT_BUTTON).click();
        return new LoginPage(getDriver());
    }

    public List<String> getMenuList() {
        List<WebElement> dropDownMenu = getDriver().findElements(By.cssSelector("#breadcrumb-menu>div:first-child>ul>li"));
        List<String> menuList = new ArrayList<>();
        for (WebElement el : dropDownMenu) {
            menuList.add(el.getAttribute("innerText"));
        }
        return menuList;
    }

    public UserPage clickUserName() {
        getDriver().findElement(By.xpath("//div[3]/a[1]/span")).click();
        return new UserPage(getDriver());
    }

    public PeoplePage openPeoplePageFromDashboardDropdownMenu () {
        clickDashboardDropdownMenu();
        getDriver().findElement(By.xpath("//li/a/span[contains(text(), 'People')]")).click();
        return new PeoplePage(getDriver());
    }
}
