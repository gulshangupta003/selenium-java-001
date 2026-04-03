package constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FrameworkConstants {

    // ──── Paths ────
    public static final String CONFIG_PATH = "src/main/resources/config.properties";

    // ──── Default Timeouts (seconds) ────
    public static final int DEFAULT_IMPLICIT_WAIT = 10;
    public static final int DEFAULT_EXPLICIT_WAIT = 15;
    public static final int DEFAULT_FLUENT_WAIT = 30;
    public static final int DEFAULT_PAGE_LOAD_TIMEOUT = 30;
    public static final int DEFAULT_RETRY_COUNT = 1;
    public static final String TEST_DATA_PATH = "src/test/resources/testdata/";

    public enum BrowserType {
        CHROME,
        FIREFOX,
        EDGE
    }

    public enum Env {
        DEV,
        QA,
        PROD
    }

}
