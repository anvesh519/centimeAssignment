package com.centime.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reporter {

    private static final String SUMMARY_FILE_DIR = File.separatorChar + "target" + File.separatorChar + "site" + File.separatorChar + "serenity";
    private static final String FILE_NAME = "summary.html";
    private static Map<String, Boolean> systemInfoKeyMap = new HashMap<>();
    private static ChromeDriver driver = null;

    private Reporter() {
    }

    private static ExtentHtmlReporter getExtentHtmlReport() {
        return CustomizeReporter.getExtentHtmlReport();
    }

    private static ExtentReports getExtentReport() {
        return CustomizeReporter.getExtentReport();
    }

    public static void loadXMLConfig(String xmlPath) {
        getExtentHtmlReport().loadXMLConfig(xmlPath);
    }

    public static void loadXMLConfig(File file) {
        getExtentHtmlReport().loadXMLConfig(file.getPath());
    }

    static void addStepLog(String message) {
        getCurrentStep().info(message);
    }

    static void addWarningStepLog(String message) {
        getCurrentStep().warning(message);
    }

    static void addScenarioLog(String message) {
        getCurrentScenario().info(message);
    }

    public static void addScreenCaptureFromPath(String imagePath) throws IOException {
        getCurrentStep().addScreenCaptureFromPath(imagePath);
    }

    public static void addScreenCaptureFromPath(String imagePath, String title) throws IOException {
        getCurrentStep().addScreenCaptureFromPath(imagePath, title);
    }

    public static void addScreenCast(String screenCastPath) throws IOException {
        getCurrentStep().addScreencastFromPath(screenCastPath);
    }

    public static void setSystemInfo(String key, String value) {
        if (systemInfoKeyMap.isEmpty() || !systemInfoKeyMap.containsKey(key)) {
            systemInfoKeyMap.put(key, false);
        }

        if (!systemInfoKeyMap.get(key)) {
            getExtentReport().setSystemInfo(key, value);
            systemInfoKeyMap.put(key, true);
        }
    }

    public static void setTestRunnerOutput(List<String> log) {
        getExtentReport().setTestRunnerOutput(log);
    }

    public static void setTestRunnerOutput(String outputMessage) {
        getExtentReport().setTestRunnerOutput(outputMessage);
    }

    public static void assignAuthor(String... authorName) {
        getCurrentScenario().assignAuthor(authorName);
    }

    private static ExtentTest getCurrentStep() {
        return CustomizeReporter.stepTestThreadLocal.get();
    }

    private static ExtentTest getCurrentScenario() {
        return CustomizeReporter.scenarioThreadLocal.get();
    }

    public static void generateSummaryReport(String fileName) {
        try {
            createFile(fileName,
                    "<html><head>" + "<style>body{background: #f6f6f6;font-family: Calibri;font-size: 16px;}"
                            + ".table, td, th {border: 1px solid #ddd;" + "text-align: left;"
                            + "font-family: Calibri;" + "}" + ".table {" + "border-collapse: collapse;" + "width: 100%;"
                            + "}" + "th, td {" + "padding: 6px;" + "}" + " .alternate tr:nth-child(2n) {background-color: #F4FAF3;" +
                            "}.alternate tr {background-color: white;}.alternate tr:nth-child(2n):hover, .alternate tr:hover {background-color: #ff6847;}" +
                            ".alternate {border-collapse: collapse;" + "width: 100%;" + "</style></head><body>" + CreateExampleTable.getTable() + "</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateEmailableReport() {

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments(Arrays.asList("disable-infobars", "--start-maximized"));
        driver = new ChromeDriver(options);
        String dir = System.getProperty("user.dir") + SUMMARY_FILE_DIR;
        /*driver.get("file:///" + dir + "/index.html");
        sleep(2000);
        System.out.println("Navigating to Dashboard...");
        driver.findElement(By.xpath("//a[@view='dashboard-view']")).click();
        System.out.println("Getting snapshot of Report sections...");

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        takeSnapShot(driver, dir + "\\FullPage.png");*/
        driver.get("file:///" + dir + "/summary.html");
        sleep(2000);
        takeSnapShot(driver, dir + "\\report.png");
        /*WebElement element = driver.findElement(By.xpath("//table[@class='alternate']"));
        takeSnapShot(driver, dir + "\\Table1.png", element);*/
        System.out.println("Creating HTML file...");
        File resultFile = new File(dir + "\\emailreport.html");
        try {
            PrintWriter writer = new PrintWriter(resultFile);
            writer.write("<html>\n");
            writer.append("<body>\n");
            writer.append("<p><img src=\"report.png\"/></p>\n");
            writer.append("<br/>\n");
            writer.close();
            System.out.println("Email Report Generation Complete!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }
        /*driver.get("file:///" + dir + "summary.html");
        List<WebElement> errorTable = driver.findElements(By.xpath("//table[@class='alternate']"));
        for (int index = 1; index <= errorTable.size(); index++) {
            WebElement element = errorTable.get(index);
        }*/
    }

    private static File createFile(String fileName, String content) throws IOException {
        String dir = System.getProperty("user.dir") + SUMMARY_FILE_DIR;

        File file = new File(dir);
        if (!file.exists())
            file.mkdir();

        file = new File(dir + File.separator + fileName);
        if (file.exists())
            file.delete();

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();
        return file;
    }

    private static void takeSnapShot(ChromeDriver chromeDriver, String destinationPath) {

        File DestFile = new File(destinationPath);
        Screenshot fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportRetina(500, 0, 0, 2)).takeScreenshot(chromeDriver);
        try {
            ImageIO.write(fpScreenshot.getImage(), "PNG", DestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try {
            TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
            File srcFile = scrShot.getScreenshotAs(OutputType.FILE);                                      //storing screenshot in srcfile
            File DestFile = new File(destinationPath);                                                       //create the png file in particular folder
            FileUtils.copyFile(srcFile, DestFile);
        } catch (WebDriverException e) {
            System.out.println("webdriver Exception");
        } catch (IOException e) {
            System.out.println("InputOutput Exception");
        }*/

    }

    private static void takeSnapShot(WebDriver webdriver, String destinationPath, WebElement webElement) {

        try {
            /*File DestFile = new File(destinationPath);
            Screenshot screenshot = new AShot().takeScreenshot(webdriver, webElement);
            ImageIO.write(screenshot.getImage(), "PNG", DestFile);*/
            TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
            File srcFile = scrShot.getScreenshotAs(OutputType.FILE);                                      //storing screenshot in srcfile
            BufferedImage fullImg = ImageIO.read(srcFile);

            Point point = webElement.getLocation();                                                      // getting upper left corner of particular section
            int width = webElement.getSize().getWidth();                                                 //find width from that corner
            int height = webElement.getSize().getHeight() + 10;                                              // find height from that corner
            BufferedImage ElementScreenshot = fullImg.getSubimage(point.getX(), point.getY(), width, height);//getting subimage from complete webpage
            ImageIO.write(ElementScreenshot, "PNG", srcFile);
            File DestFile = new File(destinationPath);                                                       //create the png file in particular folder
            FileUtils.copyFile(srcFile, DestFile);
        } catch (WebDriverException e) {
            System.out.println("webdriver Exception");
        } catch (IOException e) {
            System.out.println("InputOutput Exception");
        }

    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
