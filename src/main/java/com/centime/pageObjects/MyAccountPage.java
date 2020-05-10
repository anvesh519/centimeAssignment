package com.centime.pageObjects;

import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Data
public class MyAccountPage {
    private WebDriver driver;
    private By signoutLink = By.linkText("Sign out");

    public MyAccountPage(WebDriver driver) {
        this.driver = driver;
    }
}
