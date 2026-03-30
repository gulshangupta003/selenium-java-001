package base;

import config.ConfigReader;
import driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    @Parameters({"browser"})
    @BeforeMethod
    public void setUp(@Optional String browser) {
        log.info("═══════════════════════════════════════════════");
        log.info("STARTING TEST SETUP");

        if (browser != null && !browser.isEmpty()) {
            System.setProperty("browser", browser);
            log.info("Browser override from TestNG XML: {}", browser);
        }

        DriverManager.initDriver();
        log.info("WebDriver initialized: {}", ConfigReader.getInstance().getBrowser());

        String baseUrl = ConfigReader.getInstance().getBaseUrl();
        DriverManager.getDriver().get(baseUrl);
        log.info("Navigated to: {}", baseUrl);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("TEST FAILED: {}", testName);
            log.error("Failure reason: {}", result.getThrowable().getMessage());
            takeScreenshot(testName);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("TEST PASSED: {}", testName);
        } else if (result.getStatus() == ITestResult.SKIP) {
            log.warn("TEST SKIPPED: {}", testName);
        }

        DriverManager.quitDriver();
        log.info("WebDriver closed and cleaned up");
        log.info("═══════════════════════════════════════════════");
    }

    private void takeScreenshot(String testName) {
        try {
            String screenshotDir = ConfigReader.getInstance()
                    .get("screenshot.path", "screenshots/");
            Files.createDirectories(Paths.get(screenshotDir));

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = testName + "_" + timestamp + ".png";
            Path destination = Paths.get(screenshotDir, fileName);

            File screenshot = ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), destination);

            log.info("Screenshot saved: {}", destination);
        } catch (IOException e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
        }
    }

    protected void sleep(int seconds) {
        log.warn("Using Thread.sleep for {} seconds — replace with explicit wait!", seconds);
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Sleep interrupted", e);
        }
    }

}
