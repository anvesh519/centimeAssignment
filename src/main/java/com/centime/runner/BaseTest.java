package com.centime.runner;

import com.centime.reports.Reporter;

import java.io.File;

public class BaseTest {
    private static final String EXTENT_CONFIG_FILE = "C:\\work\\UIWorkSpace\\ecart\\src\\main\\java\\com\\centime\\reports\\extent-config.xml";


    static void generateReport(String summaryReportFileName, String summaryReportFileLink) {

        Reporter.loadXMLConfig(EXTENT_CONFIG_FILE);
        Reporter.setSystemInfo("User Name", System.getProperty("user.name"));
        Reporter.setSystemInfo("Java Version", System.getProperty("java.version"));
        Reporter.setSystemInfo("OS Version", System.getProperty("os.name"));
        Reporter.generateSummaryReport(summaryReportFileName);
        Reporter.setSystemInfo("Test Scenarios", summaryReportFileLink);
        Reporter.assignAuthor("Anvesh ");
        Reporter.setTestRunnerOutput("Sample test runner output message");
    }

}
