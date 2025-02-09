package school.redrover.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import school.redrover.model.base.BaseMainHeaderPage;
import school.redrover.runner.TestUtils;

public class UserPage extends BaseMainHeaderPage<UserPage> {

    public UserPage(WebDriver driver) {
        super(driver);
    }

    public UserDeletePage clickDeleteUserBtnFromUserPage(String newUserName) {
        TestUtils.click(this, getDriver().
                findElement(By.xpath("//a[@href='/user/" + newUserName + "/delete']")));
        return new UserDeletePage(getDriver());
    }

    public ViewPage clickMyViewsDropDownMenuUser() {
        getDriver().findElement(By.xpath("//a[contains(@href, '/my-views')]")).click();
        return new ViewPage(getDriver());
    }
}


