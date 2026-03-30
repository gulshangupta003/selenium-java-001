package config;

import constants.FrameworkConstants;
import constants.FrameworkConstants.BrowserType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static ConfigReader instance;

    private final Properties properties;

    private ConfigReader() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.CONFIG_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Filed to load config.properties file from: " + FrameworkConstants.CONFIG_PATH, e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }

        return instance;
    }

    public String get(String key) {
        String systemProp = System.getProperty(key);

        if (systemProp != null) {
            return systemProp;
        }

        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property " + key + " not found in the config.properties");
        }

        return value.trim();
    }

    public String get(String key, String defaultValue) {
        String systemProp = System.getProperty(key);
        if (systemProp != null) {
            return systemProp;
        }

        return properties.getProperty(key, defaultValue).trim();
    }

    public BrowserType getBrowser() {
        return BrowserType.valueOf(get("browser", "chrome"));
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public int getImplicitWait() {
        String value = get("implicit.wait", String.valueOf(FrameworkConstants.DEFAULT_IMPLICIT_WAIT));

        return Integer.parseInt(value);
    }

    public int getExplicitWait() {
        String value = get("explicit.wait", String.valueOf(FrameworkConstants.DEFAULT_EXPLICIT_WAIT));

        return Integer.parseInt(value);
    }

    public int getFluentWait() {
        String value = get("fluent.wait", String.valueOf(FrameworkConstants.DEFAULT_FLUENT_WAIT));

        return Integer.parseInt(value);
    }

    public int getPageLoadTimeout() {
        String value = get("page.load.timeout", String.valueOf(FrameworkConstants.DEFAULT_PAGE_LOAD_TIMEOUT));

        return Integer.parseInt(value);
    }

    public String getBaseUrl() {
        return get("base.url");
    }
}
