package com.centime.reports;

import java.io.File;

public enum ExtentProperties {
    INSTANCE;

    private String reportPath;
    private String extentXServerUrl;
    private String projectName;

    ExtentProperties() {
        this.reportPath = "output" + File.separator + "Run_" + System.currentTimeMillis() + File.separator + "report.html";
        this.projectName = "default";
    }

    public String getReportPath() {
        return this.reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getExtentXServerUrl() {
        return this.extentXServerUrl;
    }

    public void setExtentXServerUrl(String extentXServerUrl) {
        this.extentXServerUrl = extentXServerUrl;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
