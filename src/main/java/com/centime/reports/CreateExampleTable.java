package com.centime.reports;

import com.centime.reports.enums.ScenarioStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CreateExampleTable {

    static String htmlTable = "";

    static void createTable() {
        CreateExampleTable.createFailedScenariosDetailsTable();
        CreateExampleTable.createExampleTableWithScenarioStatus();
    }

    private static void createFailedScenariosDetailsTable() {
        HashMap<String, List<String>> errorScenarios = CustomizeReporter.getErrorMsgs();
        if (!errorScenarios.isEmpty()) {
            StringBuilder errorTable = new StringBuilder(
                    "<table class=\"alternate\"><tr><td bgcolor=\"#4682b4\"><b>REASON FOR FAILURE</b></td><td bgcolor=\"#4682b4\"><b>SCENARIO COUNT</b></td></tr>");
            for (Map.Entry<String, List<String>> entry : errorScenarios.entrySet()) {
                errorTable.append("<tr><td>").append(entry.getKey()).append("</td>").append("<td>")
                        .append(entry.getValue().size()).append("</td>").append("</tr>");
            }
            htmlTable = htmlTable + errorTable.append("</table></br>").toString();
        }
    }

    private static void createExampleTableWithScenarioStatus() {
        List<Map<String, String>> data = CustomizeReporter.getDataTable();
        StringBuilder table = new StringBuilder("<table class=\"table\">");
        boolean isHeader = true;
        for (Map<String, String> aData : data) {
            table.append("<tr>");
            if (isHeader) {
                for (Map.Entry<String, String> entry : aData.entrySet()) {
                    table.append("<td bgcolor=\"#4682b4\"><b>").append(entry.getKey().toUpperCase())
                            .append("</b></td>");
                }
                isHeader = false;
                table.append("</tr>");
                table.append("<tr>");
            }
            if (aData.containsKey("STATUS")) {
                String backgroundColor;
                for (Map.Entry<String, String> entry : aData.entrySet()) {
                    backgroundColor = ScenarioStatus.valueOf(aData.get("STATUS")).getColor();
                    table.append("<td bgcolor=\"").append(backgroundColor).append("\">").append(entry.getValue()).append("</td>");
                }
            } else {
                for (Map.Entry<String, String> entry : aData.entrySet()) {
                    table.append("<td>").append(entry.getValue()).append("</td>");
                }
            }
            table.append("</tr>");
        }
        htmlTable = htmlTable + table.append("</table><br>").toString();
    }

    static String getTable() {
        return htmlTable;
    }
}
