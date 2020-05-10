package com.centime.stepDefinitions;

import com.centime.config.Base;
import com.centime.reports.CustomizeReporter;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks extends Base {
    @Before
    public void setUp(Scenario scenario) {

    }

    @After
    public void tearDown(Scenario scenario) {
        driver.quit();
        CustomizeReporter.generateSummaryReport(scenario);
        CustomizeReporter.setScenarioStatus(scenario);

    }

}
