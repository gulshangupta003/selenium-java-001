package utils;

import config.ConfigReader;
import constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public final class DataProviderHelper {

    private static final Logger log = LogManager.getLogger(DataProviderHelper.class);

    private DataProviderHelper() {
    }

    // ──── Excel Providers ────

    /**
     * Returns Excel data as Object[][] (positional access).
     * Usage: data[0] = first column, data[1] = second column
     */
    public static Object[][] fromExcel(String fileName, String sheetName) {
        String filePath = getDynamicTestDataFilePath(fileName);
        log.info("Loading test data from Excel: {} [{}]", filePath, sheetName);

        return ExcelUtils.getExcelData(filePath, sheetName);
    }

    /**
     * Returns Excel data as Maps (named access).
     * Usage: data.get("username"), data.get("password")
     * <p>
     * Each Map = one row. Map keys = column headers.
     * Wrapped in Object[][] because TestNG DataProvider requires this format.
     */
    public static Object[][] fromExcelAsMap(String fileName, String sheetName) {
        String filePath = getDynamicTestDataFilePath(fileName);
        log.info("Loading test data as maps from Excel: {} [{}]", fileName, sheetName);

        List<Map<String, String>> dataList = ExcelUtils.getExcelDataAsMap(filePath, sheetName);

        Object[][] data = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }

        return data;
    }

    // ──── JSON Providers ────

    /**
     * Returns JSON array data as Maps.
     */
    public static Object[][] fromJson(String fileName) {
        String filePath = getDynamicTestDataFilePath(fileName);
        log.info("Loading test data from JSON: {}", filePath);

        List<Map<String, String>> dataList = JsonUtils.getJsonData(filePath);

        return mapToDataProviderArray(dataList);
    }

    // ──── Filtered Providers ────

    /**
     * Returns only rows where a specific column matches a value.
     * <p>
     * Use case: Same Excel file has data for multiple tests.
     * Filter by "testType" column:
     * fromExcelFiltered("login_data.xlsx", "LoginTests", "testType", "positive")
     * → returns only rows where testType = "positive"
     */
    public static Object[][] fromExcelFiltered(String fileName, String sheetName,
                                               String filterColumn, String filterValue) {
        String filePath = getDynamicTestDataFilePath(fileName);
        log.info("Loading filtered data from Excel: {} [{}] where {} = {}",
                filePath, sheetName, filterColumn, filterValue);

        List<Map<String, String>> allData = ExcelUtils.getExcelDataAsMap(filePath, sheetName);

        List<Map<String, String>> filteredData = allData.stream()
                .filter(row -> filterValue.equalsIgnoreCase(row.get(filterColumn)))
                .toList();

        log.info("Filtered {} rows from {} total", filteredData.size(), allData.size());

        return mapToDataProviderArray(filteredData);
    }

    private static Object[][] mapToDataProviderArray(List<Map<String, String>> filteredData) {
        return filteredData.stream()
                .map(map -> new Object[]{map})
                .toArray(Object[][]::new);
    }

    private static String getDynamicTestDataFilePath(String fileName) {
        return FrameworkConstants.TEST_DATA_PATH
                + ConfigReader.getInstance().getEnv().name().toLowerCase() + "/"
                + fileName;
    }

}
