package com.centime.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Base
{
    public static WebDriver driver;
    public static Properties prop;

    public static WebDriver getDriver() throws IOException
    {
        try
        {
            prop = new Properties();
            FileInputStream fis = new FileInputStream("C:\\work\\UIWorkSpace\\ecart\\src\\main\\java\\com\\centime\\config\\global.properties");
            prop.load(fis);
            System.setProperty("webdriver.chrome.driver",
                "C:\\work\\UIWorkSpace\\ecart\\src\\test\\resources\\drivers\\chromedriver.exe");
            driver = new ChromeDriver();
            driver.get(prop.getProperty("url"));
            driver.manage().window().maximize();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Check getDriver method.");
        }
        return driver;
        // hello
    }
}
