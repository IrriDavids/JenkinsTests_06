package school.redrover.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import school.redrover.model.base.BaseConfigPage;

public class UserConfigPage extends BaseConfigPage<UserConfigPage,StatusUserPage> {

    public UserConfigPage(StatusUserPage statusUserPage) {
        super(statusUserPage);
    }

    public String getEmailValue(String attribute) {
        WebElement inputEmail = getDriver().findElement(By.xpath("//input[@name='email.address']"));

        return inputEmail.getAttribute(attribute);
    }

    public UserConfigPage enterEmail(String email) {
        WebElement inputEmail = getDriver().findElement(By.xpath("//input[@name='email.address']"));
        inputEmail.clear();
        inputEmail.sendKeys(email);

        return this;
    }
}
