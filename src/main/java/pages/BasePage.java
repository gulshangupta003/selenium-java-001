package pages;

import config.ConfigReader;
import driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInstance().getExplicitWait()));
    }

    // ToDo: Add all required methods
    // ──── Wait Methods (building blocks for action methods) ────

    // ──── Click Actions ────

    // ──── Input Actions ────

    // ──── Read Actions ────

    // ──── State Check Actions ────

    // ──── Dropdown Actions (native <select> elements) ────

    // ──── Multi-Element Actions ────

    // ──── Mouse Actions ────

    // ──── JavaScript Utilities ────

    // ──── Navigation ────

    // ──── Frames and Windows ────

}
