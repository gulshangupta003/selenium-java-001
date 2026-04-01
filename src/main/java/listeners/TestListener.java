package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtils;

/**
 * Global test listener — registered once in testng.xml, applies to ALL tests.
 * <p>
 * Responsibilities:
 * 1. Log test lifecycle events (start, pass, fail, skip)
 * 2. Capture screenshot on failure
 * 3. Track execution time
 * 4. Integrate with Reports
 */
public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("╔══════════════════════════════════════════════╗");
        log.info("║  TEST SUITE STARTED: {}", String.format("%-25s║", context.getName()));
        log.info("╚══════════════════════════════════════════════╝");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        log.info("──────────────────────────────────────────────");
        log.info("▶ STARTING: {}", testName);
        if (description != null && !description.isEmpty()) {
            log.info("  Description: {}", description);
        }
        log.info("  Class: {}", result.getTestClass().getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();

        log.info("✅ PASSED: {} ({}ms)", testName, duration);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();

        log.error("❌ FAILED: {} ({}ms)", testName, duration);
        log.error("  Reason: {}", result.getThrowable().getMessage());

        String screenshotPath = ScreenshotUtils.takeScreenshot(testName);
        if (screenshotPath != null) {
            log.info("  Screenshot: {}", screenshotPath);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        log.warn("⏭ SKIPPED: {}", testName);
        if (result.getThrowable() != null) {
            log.warn("  Reason: {}", result.getThrowable().getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        int total = passed + failed + skipped;

        log.info("╔══════════════════════════════════════════════╗");
        log.info("║  TEST SUITE FINISHED: {}", String.format("%-23s║", context.getName()));
        log.info("║  Total: {} | Passed: {} | Failed: {} | Skipped: {}  ║", total, passed, failed, skipped);
        log.info("╚══════════════════════════════════════════════╝");
    }

}
