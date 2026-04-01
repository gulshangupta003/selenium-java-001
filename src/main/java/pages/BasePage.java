package pages;

import config.ConfigReader;
import driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInstance().getExplicitWait()));
    }

    // ToDo: Add all required methods
    // ──── Wait Methods (building blocks for action methods) ────

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected List<WebElement> waitForAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    // ──── Click Actions ────

    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    protected void jsClick(By locator) {
        WebElement element = waitForPresence(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // ──── Input Actions ────

    protected void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.click();
        element.sendKeys(text);
    }

    protected void clearAndType(By locator, String text) {
        WebElement element = waitForClickable(locator);
        element.clear();
        element.sendKeys(text);
    }

    // ──── Read Actions ────

    protected String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    protected String getAttribute(By locator, String attribute) {
        return waitForPresence(locator).getAttribute(attribute);
    }

    protected String getInputValue(By locator) {
        return getAttribute(locator, "value");
    }

    // ──── State Check Actions ────

    protected boolean isDisplayed(By locator) {
        try {
            return waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isEnabled(By locator) {
        return waitForVisible(locator).isEnabled();
    }

    protected boolean isSelected(By locator) {
        return waitForPresence(locator).isSelected();
    }

    // ──── Dropdown Actions (native <select> elements) ────

    protected void selectByVisibleText(By locator, String text) {
        Select dropdown = new Select(waitForVisible(locator));
        dropdown.selectByVisibleText(text);
    }

    protected void selectByValue(By locator, String value) {
        Select dropdown = new Select(waitForVisible(locator));
        dropdown.selectByValue(value);
    }

    protected void selectByIndex(By locator, int index) {
        Select dropdown = new Select(waitForVisible(locator));
        dropdown.selectByIndex(index);
    }

    protected String getSelectedText(By locator) {
        Select dropdown = new Select(waitForVisible(locator));
        return dropdown.getFirstSelectedOption().getText().trim();
    }

    // ──── Multi-Element Actions ────

    protected List<WebElement> getElements(By locator) {
        return waitForAllVisible(locator);
    }

    protected int getElementCount(By locator) {
        return getElements(locator).size();
    }

    protected List<String> getAllTexts(By locator) {
        return getElements(locator).stream()
                .map(element -> element.getText().trim())
                .toList();
    }

    // ──── Mouse Actions ────

    protected void hover(By locator) {
        WebElement element = waitForVisible(locator);

        new Actions(driver).moveToElement(element).perform();
    }

    protected void doubleClick(By locator) {
        WebElement element = waitForVisible(locator);

        new Actions(driver).doubleClick(element).perform();
    }

    protected void rightClick(By locator) {
        WebElement element = waitForVisible(locator);

        new Actions(driver).contextClick(element).perform();
    }

    protected void dragAndDrop(By source, By target) {
        WebElement sourceElement = waitForVisible(source);
        WebElement targetElement = waitForVisible(target);

        new Actions(driver).dragAndDrop(sourceElement, targetElement).perform();
    }

    // ──── JavaScript Utilities ────

    protected void scrollToElement(By locator) {
        WebElement element = waitForPresence(locator);

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});");
    }

    protected void scrollToTop() {
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, 0);");
    }

    protected void scrollToBottom() {
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    // ──── Navigation ────

    protected void navigateTo(String url) {
        driver.get(url);
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }

    // ──── Frames and Windows ────

    protected void switchToFrame(By locator) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    protected void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    protected void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    protected void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
    }

    protected String getAlertText() {
        wait.until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert().getText();
    }

}
