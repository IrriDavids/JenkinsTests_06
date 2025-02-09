package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import school.redrover.model.*;
import school.redrover.runner.BaseTest;
import school.redrover.runner.TestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsersTest extends BaseTest {

    protected static final String USER_NAME = "testuser";
    protected static final String PASSWORD = "p@ssword123";
    protected static final String EMAIL = "test@test.com";
    protected static final String USER_FULL_NAME = "Test User";
    protected static final String USER_LINK = "//a[@href='user/" + USER_NAME + "/']";
    private final By USER_NAME_LINK = By.xpath(USER_LINK);
    private static final String EXPECTED_TEXT_ALERT_INCORRECT_LOGIN_AND_PASSWORD = "Invalid username or password";

    public static List<String> listText(List<WebElement> elementList) {
        List<String> stringList = new ArrayList<>();
        for (WebElement element : elementList) {
            stringList.add(element.getText());
        }
        return stringList;
    }

    @Test
    public void testCreateNewUser() {
        boolean newUser = new ManageUsersPage(getDriver())
                .navigateToManageJenkinsPage()
                .clickManageUsers()
                .clickCreateUser()
                .enterUsername(USER_NAME)
                .enterPassword(PASSWORD)
                .enterConfirmPassword(PASSWORD)
                .enterFullName(USER_FULL_NAME)
                .enterEmail(EMAIL)
                .clickCreateUserButton()
                .isUserExist(USER_NAME);

        Assert.assertTrue(newUser);
    }

    @Test
    public void testErrorIfCreateNewUserWithInvalidEmail() {
        String errorEmail = new ManageUsersPage(getDriver())
                .navigateToManageJenkinsPage()
                .clickManageUsers()
                .clickCreateUser()
                .fillUserDetails(USER_NAME, PASSWORD, USER_FULL_NAME, "test.mail.com")
                .getInvalidEmailError();

        Assert.assertEquals(errorEmail, "Invalid e-mail address",
                "The error message is incorrect or missing");
    }

    @Ignore
    @Test
    public void testErrorWhenCreateDuplicatedUser() {

        new CreateUserPage(getDriver()).createUser(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL);

        String errorDuplicatedUser = new ManageUsersPage(getDriver())
                .clickCreateUser()
                .fillUserDetails(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL)
                .getUserNameExistsError();

        Assert.assertEquals(errorDuplicatedUser, "User name is already taken",
                "The error message is incorrect or missing");
    }

    @Ignore
    @Test
    public void testAddDescriptionToUserOnUserStatusPage() {
        final String displayedDescriptionText = "Test User Description";

        new CreateUserPage(getDriver())
                .createUser(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL);

        new ManageUsersPage(getDriver())
                .clickUserIDName(USER_NAME);

        String actualDisplayedDescriptionText = new StatusUserPage(getDriver())
                .clickAddDescriptionLink()
                .clearDescriptionInputField()
                .enterDescription(displayedDescriptionText)
                .clickSaveButton()
                .getDescription();

        Assert.assertEquals(actualDisplayedDescriptionText, displayedDescriptionText);
    }

    @Ignore
    @Test(dependsOnMethods = "testAddDescriptionToUserOnUserStatusPage")
    public void testEditDescriptionToUserOnUserStatusPage() {
        final String displayedDescriptionText = "User Description Updated";

        new MainPage(getDriver())
                .navigateToManageJenkinsPage()
                .clickManageUsers();

        new ManageUsersPage(getDriver())
                .clickUserIDName(USER_NAME);

        StatusUserPage statusUserPage = new StatusUserPage(getDriver());
        String existingDescriptionText = statusUserPage
                .clickAddDescriptionLink()
                .getDescriptionText();

        String actualDisplayedDescriptionText = statusUserPage
                .clearDescriptionInputField()
                .enterDescription(displayedDescriptionText)
                .clickSaveButton()
                .getDescription();

        Assert.assertEquals(actualDisplayedDescriptionText, displayedDescriptionText);
        Assert.assertNotEquals(actualDisplayedDescriptionText, existingDescriptionText);
    }

    @Test
    public void testAddDescriptionToUserOnTheUserProfilePage() {
        String descriptionText = new ManageUsersPage(getDriver())
                .navigateToManageJenkinsPage()
                .clickManageUsers()
                .clickUserEditButton()
                .enterDescriptionText()
                .clickYesButton()
                .getDescriptionText();

        Assert.assertEquals("Description text", descriptionText);
    }

    @Test
    public void testEditEmailOnTheUserProfilePageByDropDown() {
        final String displayedEmail = "testedited@test.com";

        new CreateUserPage(getDriver())
                .createUser(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL);

        new ManageUsersPage(getDriver())
                .clickUserIDDropDownMenu(USER_NAME)
                .selectConfigureUserIDDropDownMenu();

        UserConfigPage configureUserPage = new UserConfigPage(new StatusUserPage(getDriver()));

        String oldEmail = configureUserPage.getEmailValue("value");

        String actualEmail = configureUserPage
                .enterEmail(displayedEmail)
                .clickSaveButton()
                .clickConfigureSideMenu()
                .getEmailValue("value");

        Assert.assertNotEquals(actualEmail, oldEmail);
        Assert.assertEquals(actualEmail, displayedEmail);
    }

    @Ignore
    @Test
    public void testVerifyUserPageMenu() {
        new CreateUserPage(getDriver()).createUser(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL);

        List<String> listMenuExpected = Arrays.asList("People", "Status", "Builds", "Configure", "My Views", "Delete");

        getDriver().findElement(USER_NAME_LINK).click();

        getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.id("description")));
        List<WebElement> listMenu = getDriver().findElements(By.className("task"));

        for (int i = 0; i < listMenu.size(); i++) {
            Assert.assertEquals(listMenu.get(i).getText(), listMenuExpected.get(i));
        }
    }

    @Test
    public void testViewPeoplePage() {
        getDriver().findElement(By.xpath("//span/a[@href='/asynchPeople/']")).click();
        WebElement nameOfPeoplePageHeader = getDriver().findElement(By.xpath("//h1"));

        Assert.assertEquals(nameOfPeoplePageHeader.getText(), "People");
    }

    @Test
    public void testViewIconButtonsPeoplePage() {
        List<String> expectedIconButtonsNames = List.of("S" + "\n" + "mall", "M" + "\n" + "edium", "L" + "\n" + "arge");

        getDriver().findElement(By.xpath("//span/a[@href='/asynchPeople/']")).click();
        List<WebElement> iconButtons = getDriver().findElements(By.xpath("//div[@class='jenkins-icon-size']//ol/li"));
        List<String> actualIconButtonsNames = listText(iconButtons);

        Assert.assertEquals(actualIconButtonsNames, expectedIconButtonsNames);
    }

    @Test
    public void testSortArrowModeChangesAfterClickingSortHeaderButton() {

        boolean userIDButtonWithoutArrow = new MainPage(getDriver())
                .clickPeopleOnLeftSideMenu()
                .isUserIDButtonWithoutArrow();

        Assert.assertTrue(userIDButtonWithoutArrow, "UserID button has sort arrow");

        boolean userIDButtonWithUpArrow = new PeoplePage(getDriver())
                .clickUserIDButton()
                .isUserIDButtonWithUpArrow();

        Assert.assertTrue(userIDButtonWithUpArrow, "UserID button has not up arrow");

        boolean userIDButtonWithDownArrow = new PeoplePage(getDriver())
                .clickUserIDButton()
                .isUserIDButtonWithDownArrow();

        Assert.assertTrue(userIDButtonWithDownArrow, "UserID button has not down arrow");

        boolean userIDButtonNotContainsArrow = new PeoplePage(getDriver())
                .clickNameButton()
                .isUserIDButtonWithoutArrow();

        Assert.assertTrue(userIDButtonNotContainsArrow, "UserID button has sort arrow");
    }

    @Test
    public void testSearchPeople() {
        new CreateUserPage(getDriver()).createUser(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL);

        WebElement searchField = getDriver().findElement(
                By.xpath("//input[@name='q']"));
        searchField.sendKeys(USER_NAME);
        searchField.sendKeys(Keys.RETURN);

        WebElement actualUserName = getDriver().findElement(
                By.xpath("//div[contains(text(), 'Jenkins User ID:')]"));

        Assert.assertEquals(actualUserName.getText(), "Jenkins User ID: " + USER_NAME);
    }

    @Test
    public void testDeleteUserViaPeopleMenu() {
        String newUserName = "testuser";
        new CreateUserPage(getDriver())
                .createUserAndReturnToMainPage(newUserName, PASSWORD, USER_FULL_NAME, EMAIL);

        boolean isUserDeleted = new MainPage(getDriver())
                .clickPeopleOnLeftSideMenu()
                .clickUserName(newUserName)
                .clickDeleteUserBtnFromUserPage(newUserName)
                .clickOnYesButton()
                .clickPeopleOnLeftSideMenu()
                .checkIfUserWasDeleted(newUserName);

        Assert.assertTrue(isUserDeleted);
    }

    @Test(dependsOnMethods = "testCreateNewUser")
    public void testDeleteUserByDeleteButton() {
        boolean userNotFound = new ManageUsersPage(getDriver())
                .navigateToManageJenkinsPage()
                .clickManageUsers()
                .clickDeleteUser()
                .clickYesButton()
                .getUserDeleted(USER_NAME);

        Assert.assertFalse(userNotFound);
    }

    @Test
    public void testDeleteUserViaManageUsers() {

        new CreateUserPage(getDriver()).createUserAndReturnToMainPage(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL);

        boolean userIsNotFind = new MainPage(getDriver())
                .navigateToManageJenkinsPage()
                .clickManageUsers()
                .clickDeleteUser()
                .clickYesButton()
                .isUserExist(USER_NAME);

        Assert.assertFalse(userIsNotFind);
    }

    @Test(dependsOnMethods = "testDeleteUserViaManageUsers")
    public void testLogInWithDeletedUserCredentials() {

        getDriver().findElement(By.xpath("//a[@href= '/logout']")).click();
        getDriver().findElement(By.id("j_username")).sendKeys(USER_NAME);
        getDriver().findElement(By.xpath("//input[@name='j_password']")).sendKeys(PASSWORD);
        getDriver().findElement(By.xpath("//button[@name='Submit']")).click();

        Assert.assertEquals(getDriver().findElement(By
                        .xpath("//div[contains(@class, 'alert-danger')]")).getText(),
                "Invalid username or password");
    }

    @Test
    public void testUserCanLoginToJenkinsWithCreatedAccount() {
        String nameProject = "Engineer";
        new CreateUserPage(getDriver())
                .createUser(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL);
        new MainPage(getDriver())
                .getHeader()
                .clickLogoutButton()
                .enterUsername(USER_NAME)
                .enterPassword(PASSWORD)
                .enterSignIn(new MainPage(getDriver()));
        TestUtils.createFreestyleProject(this, nameProject, true);
        String actualResult = new MainPage(getDriver()).getProjectName().getText();

        Assert.assertEquals(actualResult, nameProject);
    }

    @Test
    public void testInputtingAnIncorrectUsername() {
        new CreateUserPage(getDriver())
                .createUser(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL);
        String actualTextAlertIncorrectUsername = new MainPage(getDriver())
                .getHeader()
                .clickLogoutButton()
                .enterUsername("incorrect user name")
                .enterPassword(PASSWORD)
                .enterSignIn(new LoginPage(getDriver()))
                .getTextAlertIncorrectUsernameOrPassword();

        Assert.assertEquals(actualTextAlertIncorrectUsername, EXPECTED_TEXT_ALERT_INCORRECT_LOGIN_AND_PASSWORD);
    }

    @Test
    public void testInputtingAnIncorrectPassword() {
        new CreateUserPage(getDriver())
                .createUser(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL);
        String actualTextAlertIncorrectPassword = new MainPage(getDriver())
                .getHeader()
                .clickLogoutButton()
                .enterUsername(USER_NAME)
                .enterPassword("12345hi")
                .enterSignIn(new LoginPage(getDriver()))
                .getTextAlertIncorrectUsernameOrPassword();

        Assert.assertEquals(actualTextAlertIncorrectPassword, EXPECTED_TEXT_ALERT_INCORRECT_LOGIN_AND_PASSWORD);
    }

    @Test
    public void  testInputtingAnIncorrectUsernameAndPassword() {
        new CreateUserPage(getDriver())
                .createUser(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL);
        String actualTextAlertIncorrectUsernameAndPassword = new MainPage(getDriver())
                .getHeader()
                .clickLogoutButton()
                .enterUsername("incorrect user name")
                .enterPassword("12345hi")
                .enterSignIn(new LoginPage(getDriver()))
                .getTextAlertIncorrectUsernameOrPassword();

        Assert.assertEquals(actualTextAlertIncorrectUsernameAndPassword, EXPECTED_TEXT_ALERT_INCORRECT_LOGIN_AND_PASSWORD);
    }
  
    @Test
    public void testCreateUserFromManageUser() {

        new CreateUserPage(getDriver())
                .createUser(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL);

        new MainPage(getDriver())
                .getHeader()
                .clickLogoutButton();

        new LoginPage(getDriver())
                .enterUsername(USER_NAME)
                .enterPassword(PASSWORD)
                .enterSignIn(new LoginPage(getDriver()));

        Assert.assertEquals(getDriver().getTitle(), "Dashboard [Jenkins]");
        Assert.assertEquals(getDriver().findElement(By.xpath("//div[@class = 'login page-header__hyperlinks']/a[1]/span")).getText(), USER_FULL_NAME);
    }

    @Test
    public void testCreateUserCheckInPeople() {

        new CreateUserPage(getDriver())
                .createUser(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL);

        new MainPage(getDriver())
                .getHeader()
                .clickLogo()
                .clickPeopleOnLeftSideMenu();

        Assert.assertEquals(getDriver().getTitle(), "People - [Jenkins]");
        Assert.assertTrue(getDriver().findElement(By.xpath("//table[@id = 'people']/tbody")).getText().contains(USER_NAME), "true");
        Assert.assertTrue(getDriver().findElement(By.xpath("//table[@id = 'people']/tbody")).getText().contains(USER_FULL_NAME), "true");
    }

    @Test
    public void testCreateUserCheckInManageUsers() {

        new CreateUserPage(getDriver())
                .createUser(USER_NAME, PASSWORD, USER_FULL_NAME, EMAIL);

        new MainPage(getDriver())
                .getHeader()
                .clickLogo()
                .navigateToManageJenkinsPage()
                .clickManageUsers();

        Assert.assertEquals(getDriver().getTitle(), "Users [Jenkins]");
        Assert.assertEquals(getDriver().findElement(By.xpath("//table[@class = 'jenkins-table sortable']/tbody/tr[last()]//a")).getText(), USER_NAME);
        Assert.assertEquals(getDriver().findElement(By.xpath("//table[@class = 'jenkins-table sortable']/tbody/tr[last()]//td[3]")).getText(), USER_FULL_NAME);
    }

   @Test
    public void testVerifyCreateUserButton() {
        String buttonName = new ManageUsersPage(getDriver())
        .navigateToManageJenkinsPage()
        .clickManageUsers()
        .getButtonText();

        Assert.assertEquals(buttonName, "Create User");
    }
    @Test
    public void testCreateUserButtonClickable() {
        String iconName = new ManageUsersPage(getDriver())
                .navigateToManageJenkinsPage()
                .clickManageUsers()
                .clickCreateUser()
                .getActualIconName();

        Assert.assertEquals(iconName, "Create User");
    }
}
