package com.centime.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import cucumber.runtime.ScenarioImpl;
import gherkin.deps.com.google.gson.JsonElement;
import gherkin.deps.com.google.gson.JsonObject;
import gherkin.deps.com.google.gson.JsonParser;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CustomizeReporter implements Reporter, Formatter {

    static ThreadLocal<ExtentTest> scenarioThreadLocal = new InheritableThreadLocal<>();
    static ThreadLocal<ExtentTest> stepTestThreadLocal = new InheritableThreadLocal<>();
    private static ExtentReports extentReports;
    private static ExtentHtmlReporter htmlReporter;
    private static ThreadLocal<ExtentTest> featureTestThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> scenarioOutlineThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<LinkedList<Step>> stepListThreadLocal = new InheritableThreadLocal<>();
    private static List<Map<String, String>> dataTable = null;
    private static List<Map<String, String>> scenarioStatusList = null;
    private static boolean scenarioOutlineBackupFlag = false;
    private static HashMap<String, List<String>> errorMsgs = new HashMap<>();
    private boolean scenarioOutlineFlag;

    public CustomizeReporter(File file) {
        setExtentHtmlReport(file);
        setExtentReport();
        setStatusHierarchy();
        stepListThreadLocal.set(new LinkedList<>());
        this.scenarioOutlineFlag = false;
    }

    private static void setStatusHierarchy() {
        List<Status> statusHierarchy = Arrays.asList(
                Status.FATAL,
                Status.FAIL,
                Status.ERROR,
                Status.PASS,
                Status.WARNING,
                Status.SKIP,
                Status.DEBUG,
                Status.INFO
        );
        getExtentReport().config().statusConfigurator().setStatusHierarchy(statusHierarchy);
    }

    static ExtentHtmlReporter getExtentHtmlReport() {
        return htmlReporter;
    }

    private static void setExtentHtmlReport(File file) {
        if (htmlReporter == null) {
            if (file == null || StringUtils.isEmpty(file.getPath())) {
                file = new File(ExtentProperties.INSTANCE.getReportPath());
            }

            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }

            htmlReporter = new ExtentHtmlReporter(file);
        }
    }

    private static void setExtentReport() {
        if (extentReports == null) {
            extentReports = new ExtentReports();
            ExtentProperties extentProperties = ExtentProperties.INSTANCE;
            if (extentProperties.getExtentXServerUrl() != null) {
                String extentXServerUrl = extentProperties.getExtentXServerUrl();

                try {
                    URL url = new URL(extentXServerUrl);
                    ExtentXReporter xReporter = new ExtentXReporter(url.getHost());
                    xReporter.config().setServerUrl(extentXServerUrl);
                    xReporter.config().setProjectName(extentProperties.getProjectName());
                    extentReports.attachReporter(htmlReporter, xReporter);
                } catch (MalformedURLException var4) {
                    throw new IllegalArgumentException("Invalid ExtentX Server URL", var4);
                }
            } else {
                extentReports.attachReporter(htmlReporter);
            }
        }
    }

    static ExtentReports getExtentReport() {
        return extentReports;
    }

    static List<Map<String, String>> getDataTable() {
        return dataTable;
    }

    private static void setDataTable(List<Map<String, String>> dataTable) {
        CustomizeReporter.dataTable = dataTable;
    }

    private static void clearErrorMessageMap() {
        CustomizeReporter.errorMsgs.clear();
    }

    private static List<Map<String, String>> getScenarioStatusList() {
        return scenarioStatusList;
    }

    private static void setScenarioStatusList(List<Map<String, String>> scenarioStatusList) {
        CustomizeReporter.scenarioStatusList = scenarioStatusList;
    }

    public static void setScenarioStatus(cucumber.api.Scenario scenario) {
        String scenarioStatus = scenario.getStatus();
        Map<String, String> dataMap = new LinkedHashMap<>();
        if (StringUtils.equalsIgnoreCase(scenarioStatus, "PASSED"))
            dataMap.put("STATUS", "PASS");
        else if (StringUtils.equalsIgnoreCase(scenarioStatus, "FAILED"))
            dataMap.put("STATUS", "FAIL");
        else if (StringUtils.equalsIgnoreCase(scenarioStatus, "PENDING"))
            dataMap.put("STATUS", "SKIP");
        if (CustomizeReporter.getScenarioStatusList() == null) {
            CustomizeReporter.setScenarioStatusList(new LinkedList<>());
        }
        CustomizeReporter.getScenarioStatusList().add(dataMap);
    }

    public static void generateSummaryReport(cucumber.api.Scenario scenario) {
        // errorMsgs.clear();
        if (!scenario.isFailed()) return;
        try {
            if (scenarioOutlineBackupFlag) {
                List<Object> steps;
                Field field1;
                field1 = ScenarioImpl.class.getDeclaredField("stepResults");
                field1.setAccessible(true);
                steps = (ArrayList<Object>) field1.get((scenario));
                for (Object temp : steps) {
                    if (((Result) temp).getStatus().equals("failed")) {
                        JsonObject object = null;
                        String errorMessage = ((Result) temp).getError().getMessage();
                        if (StringUtils.isEmpty(errorMessage)) {
                            errorMessage = ((Result) temp).getError().toString();
                        } else {
                            if (StringUtils.contains(errorMessage, "{") && StringUtils.contains(errorMessage, "}")) {
                                JsonElement element = new JsonObject();
                                JsonParser json = new JsonParser();
                                if (isJSONValid(errorMessage))
                                    element = json.parse(errorMessage);
                                else if (!errorMessage.startsWith("{")&&!errorMessage.contains("{"))
                                    element = json.parse("{" + StringUtils.substringBetween(errorMessage, "{", "}") + "}");
                                if (element.isJsonObject()) {
                                    object = element.getAsJsonObject();
                                } else if (element.isJsonArray()) {
                                    object = element.getAsJsonArray().get(0).getAsJsonObject();
                                }
                                if (object.has("errorMessage")) {
                                    errorMessage = object.get("errorMessage").getAsString();
                                } else if (object.has("error")) {
                                    errorMessage = object.get("error").getAsString();
                                } else if (object.has("errorDescription")) {
                                    errorMessage = object.get("errorDescription").getAsString();
                                } else if (object.has("message")) {
                                    errorMessage = object.get("message").getAsString();
                                } else if(object.toString().equals("{}")){
                                    errorMessage="Error Message is: "+ errorMessage;
                                }else {
                                    errorMessage = "unable to capture error message from - " + object.toString();
                                }
                            }
                        }

                        List<String> scenarios = new ArrayList<>();
                        if (StringUtils.containsIgnoreCase(errorMessage, "\r\n")) {
                            errorMessage = Stream.of(errorMessage.split("\r\n"))
                                    .filter(s -> !s.isEmpty() && !s.contains("at "))
                                    .collect(Collectors.joining("<br>"));
                        }
                        if (errorMsgs.containsKey(errorMessage)) {
                            scenarios = errorMsgs.get(errorMessage);
                        }
                        scenarios.add(scenario.getName());
                        errorMsgs.put(errorMessage, scenarios);
                        break;
                    }
                }
                setErrorMsgs(errorMsgs);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    static HashMap<String, List<String>> getErrorMsgs() {
        return errorMsgs;
    }

    private static void setErrorMsgs(HashMap<String, List<String>> errorMsgs) {
        CustomizeReporter.errorMsgs = errorMsgs;
    }

    private static boolean isJSONValid(String jsonInString) {
        Gson gson = new Gson();
        try {
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }

    public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {
    }

    public void uri(String uri) {
    }

    public void feature(Feature feature) {
        CustomizeReporter.setScenarioStatusList(null);
        featureTestThreadLocal.set(getExtentReport()
                .createTest(com.aventstack.extentreports.gherkin.model.Feature.class, feature.getName()));
        ExtentTest test = featureTestThreadLocal.get();

        for (Tag tag : feature.getTags()) {
            test.assignCategory(tag.getName());
            CreateExampleTable.htmlTable = CreateExampleTable.htmlTable + "<div><b>" + tag.getName() + "</b></div>";
        }

    }

    public void scenarioOutline(ScenarioOutline scenarioOutline) {
        this.scenarioOutlineFlag = true;
        CustomizeReporter.scenarioOutlineBackupFlag = true;
        createPreviousScenarioOutlineDataTable();
        ExtentTest node = (featureTestThreadLocal.get()).createNode(
                com.aventstack.extentreports.gherkin.model.ScenarioOutline.class, scenarioOutline.getName());
        scenarioOutlineThreadLocal.set(node);
        CreateExampleTable.htmlTable = CreateExampleTable.htmlTable + "<br><div><b>" +
                scenarioOutline.getName().toUpperCase() + "</b></div>";
    }

    private void createPreviousScenarioOutlineDataTable() {
        eof();
    }

    public void examples(Examples examples) {
        List<ExamplesTableRow> rows = examples.getRows();
        int rowSize = rows.size();
        Map<String, String> tempMap = null;

        for (int i = 0; i < rowSize; i++) {
            ExamplesTableRow examplesTableRow = rows.get(i);
            List<String> cells = examplesTableRow.getCells();
            //int cellSize = cells.size();
            if (dataTable == null) {
                dataTable = new LinkedList<>();
            }
            Map<String, String> dataMap = new LinkedHashMap<>();
            if (i == 0) {
                for (String s : cells) {
                    dataMap.put(s, null);
                    tempMap = dataMap;
                }
            } else {
                int cell = 0;
                if (tempMap != null) {
                    for (Map.Entry<String, String> entry : tempMap.entrySet()) {
                        dataMap.put(entry.getKey(), cells.get(cell++));
                    }
                }
            }
            dataTable.add(dataMap);
        }
        dataTable.remove(dataTable.size() - rowSize);
        setDataTable(dataTable);
    }

    public void startOfScenarioLifeCycle(Scenario scenario) {
        if (this.scenarioOutlineFlag) {
            this.scenarioOutlineFlag = false;
        }

        ExtentTest scenarioNode;
        if (scenarioOutlineThreadLocal.get() != null
                && scenario.getKeyword().trim().equalsIgnoreCase("Scenario Outline")) {
            scenarioNode = (scenarioOutlineThreadLocal.get())
                    .createNode(com.aventstack.extentreports.gherkin.model.Scenario.class, scenario.getName());
        } else {
            scenarioNode = (featureTestThreadLocal.get())
                    .createNode(com.aventstack.extentreports.gherkin.model.Scenario.class, scenario.getName());
        }

        for (Tag tag : scenario.getTags()) {
            scenarioNode.assignCategory(tag.getName());
        }

        scenarioThreadLocal.set(scenarioNode);
    }

    public void background(Background background) {
    }

    public void scenario(Scenario scenario) {
    }

    public void step(Step step) {
        if (!this.scenarioOutlineFlag) {
            (stepListThreadLocal.get()).add(step);
        }
    }

    public void endOfScenarioLifeCycle(Scenario scenario) {
    }

    public void done() {
        getExtentReport().flush();
    }

    public void close() {
    }

    public void eof() {
        if (scenarioOutlineBackupFlag && CustomizeReporter.getDataTable() != null) {
            List<Map<String, String>> data = CustomizeReporter.getDataTable();
            List<Map<String, String>> tempData = new LinkedList<>();
            IntStream.range(0, data.size()).forEach(index -> {
                Map<String, String> dataMap = new LinkedHashMap<>();
                for (Map.Entry<String, String> entry : data.get(index).entrySet()) {
                    dataMap.put(entry.getKey(), entry.getValue());
                }
                if (getScenarioStatusList() != null) {
                    for (Map.Entry<String, String> entry : getScenarioStatusList().get(index).entrySet()) {
                        dataMap.put(entry.getKey(), entry.getValue());
                    }
                }
                tempData.add(dataMap);
            });

            CustomizeReporter.setDataTable(tempData);
            CreateExampleTable.createTable();
            CustomizeReporter.setDataTable(null);
            CustomizeReporter.clearErrorMessageMap();
            CustomizeReporter.setScenarioStatusList(null);
        }
    }

    public void before(Match match, Result result) {
    }

    public void result(Result result) {
        if (!this.scenarioOutlineFlag) {
            if ("passed".equals(result.getStatus())) {
                (stepTestThreadLocal.get()).pass("passed");
            } else if ("failed".equals(result.getStatus())) {
                (stepTestThreadLocal.get()).fail(result.getError());
            } else if (Result.SKIPPED.equals(result)) {
                (stepTestThreadLocal.get()).skip(Result.SKIPPED.getStatus());
            } else if (Result.UNDEFINED.equals(result)) {
                (stepTestThreadLocal.get()).skip(Result.UNDEFINED.getStatus());
            }
        }
    }

    public void after(Match match, Result result) {
    }

    public void match(Match match) {
        Step step = (Step) ((LinkedList) stepListThreadLocal.get()).poll();
        String[][] data = null;
        if (Objects.requireNonNull(step, "step can not be null").getRows() != null) {
            List<DataTableRow> rows = step.getRows();
            int rowSize = rows.size();

            for (int i = 0; i < rowSize; ++i) {
                DataTableRow dataTableRow = rows.get(i);
                List<String> cells = dataTableRow.getCells();
                int cellSize = cells.size();
                if (data == null) {
                    data = new String[rowSize][cellSize];
                }

                for (int j = 0; j < cellSize; ++j) {
                    data[i][j] = cells.get(j);
                }
            }
        }

        ExtentTest scenarioTest = scenarioThreadLocal.get();
        ExtentTest stepTest = null;

        try {
            stepTest = scenarioTest.createNode(new GherkinKeyword(step.getKeyword()),
                    step.getKeyword() + step.getName());
        } catch (ClassNotFoundException var11) {
            var11.printStackTrace();
        }

        if (data != null) {
            Markup table = MarkupHelper.createTable(data);
            Objects.requireNonNull(stepTest).info(table);
        }

        stepTestThreadLocal.set(stepTest);
    }

    public void embedding(String mimeType, byte[] data) {
    }

    public void write(String text) {
    }
}
