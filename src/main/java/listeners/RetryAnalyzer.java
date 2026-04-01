package listeners;

import config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Automatically retries failed tests up to a configured number of times.
 * Retry count is read from config.properties (default: 1).
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log = LogManager.getLogger(RetryAnalyzer.class);
    private int currentRetryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetryCount = ConfigReader.getInstance().getRetryCount();

        if (currentRetryCount < maxRetryCount) {
            String testName = result.getMethod().getMethodName();
            currentRetryCount++;
            log.warn("🔄 RETRYING: {} (attempt {}/{})",
                    testName, currentRetryCount, maxRetryCount);

            return true;
        }

        return false;
    }

}
