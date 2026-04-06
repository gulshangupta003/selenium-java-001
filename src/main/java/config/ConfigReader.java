package config;

import constants.FrameworkConstants;
import constants.FrameworkConstants.BrowserType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigReader {

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

        if (systemProp != null && !systemProp.trim().isEmpty()) {
            return systemProp.trim();
        }

        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property " + key + " not found in the config.properties");
        }

        return value.trim();
    }

    public String get(String key, String defaultValue) {
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.trim().isEmpty()) {
            return systemProp.trim();
        }

        return properties.getProperty(key, defaultValue).trim();
    }

    public BrowserType getBrowser() {
        String browser = get("browser", "chrome").toUpperCase();

        return BrowserType.valueOf(browser);
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
        FrameworkConstants.Env env = getEnv();

        return switch (env) {
            case QA -> get("qa.url");
            case DEV -> get("dev.url");
            case PROD -> get("prod.url");
            default -> get("qa.url");
        };
    }

    public FrameworkConstants.Env getEnv() {
        String env = get("env", "qa").toUpperCase();

        return FrameworkConstants.Env.valueOf(env);
    }

    public int getRetryCount() {
        String value = get("retry.count", String.valueOf(FrameworkConstants.DEFAULT_RETRY_COUNT));

        return Integer.parseInt(value);
    }

}
