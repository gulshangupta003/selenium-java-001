package utils;

import driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Advanced wait utilities for scenarios beyond BasePage's standard waits.
 * All methods are static — usable from tests, listeners, or any class.
 */
public final class WaitUtils {

    private WaitUtils() {
    }

    /**
     * Wait with CUSTOM timeout — when default explicit wait isn't enough.
     * Example: file upload might need 60 seconds, but default is 15.
     */
    public static WebElement waitForVisible(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds));

        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds));

        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement fluentWait(By locator, int timeoutSeconds, int pollingMillis) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollingMillis))
                .ignoring(NoSuchElementException.class);

        return fluentWait.until(driver -> driver.findElement(locator));
    }

    public static boolean waitForUrlContains(String urlFragment, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds));

        return wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    public static boolean waitForTextChange(By locator, String oldText, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds));

        return wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(locator, oldText)));
    }

    public static boolean waitForTextToBe(By locator, String expectedText, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds));

        return wait.until(ExpectedConditions.textToBe(locator, expectedText));
    }

    public static boolean waitForElementCount(By locator, int expectedCount, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds));

        List<WebElement> elementList = wait.until(ExpectedConditions.numberOfElementsToBe(locator, expectedCount));

        return elementList.size() == expectedCount;
    }

    /**
     * Wait for a custom condition using lambda.
     * The most flexible wait — define any boolean condition.
     * <p>
     * Example usage:
     * WaitUtils.waitForCondition(
     * driver -> driver.findElements(By.className("item")).size() > 3,
     * 10
     * );
     */
    public static <T> T waitForCondition(Function<WebDriver, T> condition, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds));

        return wait.until(condition);
    }

    /**
     * Check if element is present WITHOUT waiting.
     * Does not throw — returns true/false immediately.
     * Useful for conditional logic: "if element exists, do X"
     */
    public static boolean isElementPresent(By locator, int timeoutSeconds) {
        try {
            DriverManager.getDriver().findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
