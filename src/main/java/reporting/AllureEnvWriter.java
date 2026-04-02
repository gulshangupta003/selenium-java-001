package reporting;

import config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Generates allure-results/environment.properties before tests run.
 * This info appears in the "Environment" widget on the Allure dashboard.
 */
public final class AllureEnvWriter {

    private static final Logger log = LogManager.getLogger(AllureEnvWriter.class);

    private AllureEnvWriter() {
    }

    /**
     * Call this once during suite setup (from BaseTest or a suite listener).
     * Creates environment.properties in allure-results directory.
     */
    public static void WriteEnvInfo() {
        try {
            Files.createDirectories(Paths.get("allure-results"));

            Properties properties = new Properties();
            ConfigReader configReader = ConfigReader.getInstance();

            properties.setProperty("Browser", configReader.getBrowser().name());
            properties.setProperty("ENV", configReader.getEnv().name());
            properties.setProperty("Base URL", configReader.getBaseUrl());
            properties.setProperty("Java Version", System.getProperty("java.version"));
            properties.setProperty("OS", System.getProperty("os.name"));

            try (FileOutputStream fos = new FileOutputStream("allure-results/environment.properties")) {
                properties.store(fos, "Allure Environment Info");
            }

            log.info("Allure environment.properties written successfully");
        } catch (IOException e) {
            log.error("Failed to write Allure environment info: {}", e.getMessage());
        }
    }

}
