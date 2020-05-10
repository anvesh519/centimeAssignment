package com.centime.reports.enums;

public enum ScenarioStatus {
    PASS("#47ff9c"),
    FAIL("#ff6847"),
    SKIP("#86bff4");

    private String color;

    ScenarioStatus(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
