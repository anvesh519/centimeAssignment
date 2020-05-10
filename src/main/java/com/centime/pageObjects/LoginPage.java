package com.centime.pageObjects;

import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Data
public class LoginPage {
    WebDriver driver;
    public LoginPage(WebDriver driver){
        this.driver=driver;
    }

    private By emailInput = By.id("email_create");
    private By createAccountButton = By.id("SubmitCreate");
}
