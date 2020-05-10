package com.centime.reports;

import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.fail;

public class ReportSteps {

    private static final int WORD_WRAP_LENGTH = 100;
    private static Logger log = LoggerFactory.getLogger(ReportSteps.class);

    public static <T> void VERIFY(String reportMessage, T expectedValue, T ActualValue) {
        if (null == expectedValue) {
            if (null == ActualValue) {
                PASS(reportMessage + " is NULL as expected");
            } else {
                FAIL(reportMessage + " Error. Expected: null, Actual: " + ActualValue);
            }
        } else if (expectedValue.equals(ActualValue))
            PASS(reportMessage + ". Expected: " + expectedValue);
        else
            FAIL(reportMessage + " Error. Expected: " + expectedValue + ", Actual: " + ActualValue);
    }

    public static void FAIL(String reportMessage) {
        log.error(reportMessage);
        fail(reportMessage);
    }

    public static void PASS(String message) {
        message = "<font face=\"verdana\">PASS: " + message + "</font>";
        message = WordUtils.wrap(message, WORD_WRAP_LENGTH, "<br>", true);
        Reporter.addStepLog(message);
    }

}
