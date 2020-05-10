package com.centime.pageObjects;

import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Data
public class Registration {
    public WebDriver driver;
    private By gndr1Radio = By.id("id_gender1");
    private By gndr2Radio = By.id("id_gender2");
    private By firstName = By.id("customer_firstname");
    private By lastname = By.id("customer_lastname");
    private By email = By.id("email");
    private By password = By.id("passwd");
    private By dobDay = By.id("days");
    private By dobMonth = By.id("months");
    private By dobYear = By.id("years");
    private By newsLetterChkbox = By.id("newsletter");
    private By spclOfferChkbox = By.id("optin");
    private By addrFirstName = By.id("firstname");
    private By addrLastName = By.id("lastname");
    private By company = By.id("company");
    private By address1 = By.id("address1");
    private By address2 = By.id("address2");
    private By city = By.id("city");
    private By state = By.id("id_state");
    private By country = By.id("id_country");
    private By postCode=By.id("postcode");
    private By additionalInfo = By.id("other");
    private By homePhone = By.id("phone");
    private By mobilePhone = By.id("phone_mobile");
    private By addressAlias = By.id("alias");
    private By registerButton = By.id("submitAccount");

    public Registration(WebDriver driver) {
        this.driver = driver;
    }


}
