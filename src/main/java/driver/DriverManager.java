package driver;

import config.ConfigReader;
import constants.FrameworkConstants.BrowserType;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void initDriver() {
        BrowserType browserType = ConfigReader.getInstance().getBrowser();
        initDriver(browserType);
    }

    public static void initDriver(BrowserType browserType) {
        WebDriver driver = DriverFactory.createDriver(browserType);

        ConfigReader configReader = ConfigReader.getInstance();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(configReader.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(configReader.getPageLoadTimeout()));

        DRIVER_THREAD_LOCAL.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();

        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Call DriverManager.initDriver() first. "
                    + "This usually means @BeforeMethod hasn't run yet.");
        }

        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            driver.quit();
            DRIVER_THREAD_LOCAL.remove();
        }
    }

}
