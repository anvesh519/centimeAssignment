package com.centime.pageObjects;

import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Data
public class HomePage {
    WebDriver driver;
    private By signInLink = By.linkText("Sign in");
    System.out.println("demo");
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }


}
