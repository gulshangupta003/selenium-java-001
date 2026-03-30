package constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FrameworkConstants {

    public static final String CONFIG_PATH = "src/main/resources/config.properties";

    public enum BrowserType {
        CHROME,
        FIREFOX,
        EDGE
    }

    public enum Environment {
        DEV,
        QA,
        PROD
    }

}
