package com.centime.stepDefinitions;

import com.centime.Utilities.Utils;
import com.centime.config.Base;
import com.centime.pageObjects.HomePage;
import com.centime.pageObjects.LoginPage;
import com.centime.pageObjects.MyAccountPage;
import com.centime.pageObjects.Registration;
import com.centime.reports.ReportSteps;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class RegistrationStepDefinition extends Base {
    public WebDriver driver;
    HomePage homePage;
    Registration registration;
    LoginPage loginPage;
    MyAccountPage myAccountPage;
    private String scenario = null;
    private String emailId;
    private String firstName = null;
    private String lastName = null;
    private String password = null;


    @Given("^scenario as (.+)$")
    public void getScenario(String scenario) throws Throwable {
        this.scenario = scenario;

    }

    @Given("^user is on Home page$")
    public void user_is_on_home_landing_page() throws Throwable {
        driver = getDriver();
        homePage = new HomePage(driver);
        registration = new Registration(driver);
        loginPage = new LoginPage(driver);
        myAccountPage = new MyAccountPage(driver);
        ReportSteps.VERIFY("verify the Home page ", true, driver.findElement(homePage.getSignInLink()).isDisplayed());
        driver.findElement(homePage.getSignInLink()).click();
        Utils.pause(5);

    }

    @Given("^an Email Id to create Account$")
    public void setEmailId() throws Exception {
        this.emailId = Utils.getRandomString(10) + "@gmail.com";
        driver.findElement(loginPage.getEmailInput()).sendKeys(this.emailId);
        driver.findElement(loginPage.getCreateAccountButton()).click();
        Utils.pause(10);

    }

    @Given("^select the gender (.*)$")
    public void selectGender(String gender) {
        if (StringUtils.equalsIgnoreCase(gender, "male"))
            driver.findElement(registration.getGndr1Radio()).click();
        else
            driver.findElement(registration.getGndr2Radio()).click();

    }

    @Given("^enter firstName (.*), lastName (.*), password (.*)$")
    public void setNameFields(String firstName, String lastName, String password) {
        this.firstName = StringUtils.equalsIgnoreCase(firstName, "Mandatory") ? Utils.getRandomString(8) : "";
        this.lastName = StringUtils.equalsIgnoreCase(lastName, "Mandatory") ? Utils.getRandomString(8) : "";
        this.password = StringUtils.equalsIgnoreCase(password, "Mandatory") ? Utils.getRandomString(8) : "";

        ReportSteps.VERIFY("Verifying the Email given in homepage same as in registration form ",
                this.emailId, driver.findElement(registration.getEmail()).getAttribute("value"));
        if (StringUtils.isNotBlank(firstName) || StringUtils.isNotEmpty(firstName))
            driver.findElement(registration.getFirstName()).sendKeys(this.firstName);
        if (StringUtils.isNotEmpty(lastName) || !StringUtils.isNotBlank(password))
            driver.findElement(registration.getLastname()).sendKeys(this.lastName);
        if (!StringUtils.isEmpty(firstName) || !StringUtils.isEmpty(lastName) || !StringUtils.isEmpty(password))
            driver.findElement(registration.getPassword()).sendKeys(this.password);


    }

    @Given("^select the DOB based on number of years(.*)$")

    public void setDOB(String years) {
        if (StringUtils.containsIgnoreCase(this.scenario, "Optional")) {
            String date = Utils.getOlddate(Integer.parseInt(years.trim()));
            int day = Integer.parseInt(date.split("/")[0]);
            int month = Integer.parseInt(date.split("/")[1]);
            String year = date.split("/")[2]+"  ";
            Select dobDay = new Select(driver.findElement(registration.getDobDay()));
            dobDay.selectByIndex(day);
            Select dobMonth = new Select(driver.findElement(registration.getDobMonth()));
            dobMonth.selectByIndex(month);
            Select dobYear = new Select(driver.findElement(registration.getDobYear()));
            dobYear.selectByVisibleText(year);

        }
    }

    @Given("^signup for newsletter and specialoffers$")
    public void selectSpecilaOptions() {
        if (StringUtils.containsIgnoreCase(this.scenario, "optional")) {
            driver.findElement(registration.getSpclOfferChkbox()).click();
            driver.findElement(registration.getNewsLetterChkbox()).click();
        }

    }

    @Given(("^enter address fields address1, address2, city, state (.*), postalCode and country (.*)"))
    public void setAddress(String state, String country) {
        ReportSteps.VERIFY("Verifying firstname in personal Info and address section are same ",
                this.firstName, driver.findElement(registration.getAddrFirstName()).getAttribute("value"));
        ReportSteps.VERIFY("Verifying lastName in personal Info and address section are same ",
                this.lastName, driver.findElement(registration.getAddrLastName()).getAttribute("value"));

        driver.findElement(registration.getAddress1()).sendKeys(Utils.getRandomString(7));
        driver.findElement(registration.getCity()).sendKeys(Utils.getRandomString(8));
        driver.findElement(registration.getPostCode()).sendKeys(Utils.getRandomNumber(5));
        Select stateDrpdwn = new Select(driver.findElement(registration.getState()));
        stateDrpdwn.selectByVisibleText(state);
        Select countryDrpdwn = new Select(driver.findElement(registration.getCountry()));
        countryDrpdwn.selectByVisibleText(country);
        if (StringUtils.containsIgnoreCase(this.scenario, "Optional")) {
            driver.findElement(registration.getAddress2()).sendKeys(Utils.getRandomString(7));
            driver.findElement(registration.getCompany()).sendKeys(Utils.getRandomString(7));


        }

    }

    @Given("^enter either home or mobile number (.*)$")
    public void setPhoneNumber(boolean isMobile) {
        if (isMobile) {
            driver.findElement(registration.getMobilePhone()).sendKeys(Utils.getRandomNumber(10));
        } else
            driver.findElement(registration.getHomePhone()).sendKeys(Utils.getRandomNumber(10));
    }

    @When("^click on register$")
    public void clickOnRegister() throws Exception {
        driver.findElement(registration.getRegisterButton()).click();
        Utils.pause(10);
    }

    @Then("^verify account created successfully")
    public void verifyAccountCreation() {
        if(!StringUtils.containsIgnoreCase(this.scenario,"Negative")) {
            if (driver.findElement(myAccountPage.getSignoutLink()).isDisplayed())
                ReportSteps.PASS("Sign Out link exists , Account creation successful");
            else
                ReportSteps.FAIL("Sign Out link doesn't exists , Account creation is not successful");
        }
        else{
            ;
            ReportSteps.VERIFY("Verifying Error message ,without First name","firstname is required.",driver.findElement(By.xpath("//div[@id='center_column']//li[1]")).getText() );
        }

    }

}

