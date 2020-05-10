package com.centime.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions( monochrome = true,
            strict = true,
            plugin = { "com.centime.reports.CustomizeReporter:target/site/serenity/index.html" })
// ~@smoke,~@regression,~@piv,~@mobileTest,~@webTest,smoke
public class TestRunner extends BaseTest
{
    private static final String SUMMARY_REPORT_FILE_NAME = "summary.html";
    private static final String SUMMARY_REPORT_FILE_LINK = "<a href=\"./summary.html\" target=\"_blank\">Test Scenarios</a>";

    @AfterClass
    public static void generateReport() {
        generateReport(SUMMARY_REPORT_FILE_NAME, SUMMARY_REPORT_FILE_LINK);
    }
}
