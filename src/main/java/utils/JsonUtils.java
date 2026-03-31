package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static final Logger log = LogManager.getLogger(JsonUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {

    }

    /**
     * Reads JSON array into List of Maps.
     * <p>
     * JSON file:
     * [
     * {"username": "standard_user", "password": "secret_sauce"},
     * {"username": "locked_out_user", "password": "secret_sauce"}
     * ]
     */
    public static List<Map<String, String>> getJsonData(String filePath) {
        try {
            List<Map<String, String>> data = mapper.readValue(
                    new File(filePath),
                    new TypeReference<>() {
                    }
            );
            log.info("Read {} records from {}", data.size(), filePath);

            return data;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    /**
     * Reads JSON into a specific POJO class.
     * Example: TestConfig config = JsonUtils.getJsonAsObject("config.json", TestConfig.class);
     */
    public static <T> T getJsonAsObject(String filePath, Class<T> clazz) {
        try {
            return mapper.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON: " + filePath, e);
        }
    }

    /**
     * Reads a specific key from a JSON object.
     * File: {"qa": {"url": "https://qa.example.com"}, "prod": {"url": "https://example.com"}}
     * Usage: getJsonValue("environments.json", "qa", "url") → "https://qa.example.com"
     */
    public static String getJsonValue(String filePath, String... keys) {
        try {
            JsonNode node = mapper.readTree(new File(filePath));
            for (String key : keys) {
                node = node.get(key);

                if (node == null) {
                    throw new RuntimeException("Key '" + key + "' not found in " + filePath);
                }
            }

            return node.asText();
        } catch (IOException e) {
            throw new RuntimeException("Files to load JSON file: " + filePath, e);
        }
    }

    /**
     * Converts JSON array to Object[][] for TestNG DataProvider.
     */
    public static Object[][] getJsonDataAsArray(String filePath) {
        List<Map<String, String>> data = getJsonData(filePath);

        Object[][] result = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }

        return result;
    }

}
