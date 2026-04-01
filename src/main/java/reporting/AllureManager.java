package reporting;

import driver.DriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

/**
 * Centralized Allure reporting utility.
 * Provides methods to attach screenshots, text, and logs to Allure reports.
 */
public final class AllureManager {

    private static final Logger log = LogManager.getLogger(AllureManager.class);

    private AllureManager() {
    }

    /**
     * Attaches a screenshot to the current Allure test step.
     * Uses @Attachment annotation — Allure automatically captures the return value.
     * <p>
     * The annotation approach:
     * - Return type must be byte[]
     * - Allure intercepts the return value via AspectJ
     * - Screenshot appears inline in the report
     */
    @Attachment(value = "Screenshot on failure", type = "image/png")
    public static byte[] attachScreenshot() {
        try {
            return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Failed to capture screenshot for Allure: {}", e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Alternative approach using Allure lifecycle API.
     * More control — you can name the screenshot dynamically.
     */
    public static void attachScreenshotWithName(String name) {
        try {
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), ".png");
        } catch (Exception e) {
            log.error("Failed to capture screenshot for Allure: {}", e.getMessage());
        }
    }

    /**
     * Attach any text content to the report.
     * Useful for: API responses, page source, log snippets.
     */
    @Attachment(value = "{0}", type = "text/plain")
    public static String attachText(String content) {
        return content;
    }

    /**
     * Attach page source on failure — helps debug when screenshot isn't enough.
     */
    public static void attachPageSource() {
        try {
            String pageSource = DriverManager.getDriver().getPageSource();
            Allure.addAttachment("Page Source", "text/html", pageSource);
        } catch (Exception e) {
            log.error("Failed to attach page source: {}", e.getMessage());
        }
    }

    /**
     * Attach current URL — useful context for debugging failures.
     */
    @Attachment(value = "Current URL", type = "text/plain")
    public static String attachCurrentUrl() {
        try {
            return DriverManager.getDriver().getCurrentUrl();
        } catch (Exception e) {
            return "Failed to get URL";
        }
    }

    /**
     * Add environment information to the report.
     * Call once during suite setup.
     */
    public static void addEnvironmentInfo(String browser, String environment, String baseUrl) {
        Allure.parameter("Browser", browser.toUpperCase());
        Allure.parameter("ENV", environment.toUpperCase());
        Allure.parameter("Base URL", baseUrl);
    }

}
