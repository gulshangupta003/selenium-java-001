package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ExcelUtils {

    private static final Logger log = LogManager.getLogger(ExcelUtils.class);

    private ExcelUtils() {

    }

    /**
     * Reads Excel sheet into a 2D Object array.
     * Format required by TestNG @DataProvider.
     * <p>
     * Row 0 = headers (skipped)
     * Row 1+ = data rows
     * <p>
     * Example Excel:
     * | username       | password     | expected    |
     * | standard_user  | secret_sauce | success     |
     * | locked_out     | secret_sauce | locked_error|
     * <p>
     * Returns:
     * Object[][] {
     * {"standard_user", "secret_sauce", "success"},
     * {"locked_out", "secret_sauce", "locked_error"}
     * }
     */
    public static Object[][] getExcelData(String filePath, String sheetName) {
        try (
                FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)
        ) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

            // Skip header row (index 0), data starts from row 1
            Object[][] data = new Object[rowCount - 1][colCount];

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    data[i - 1][j] = getCellValueAsString(row.getCell(j));
                }
            }
            log.info("Read {} rows from sheet '{}' in {}", rowCount - 1, sheetName, filePath);

            return data;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
    }

    /**
     * Reads Excel as List of Maps — each row becomes a Map with header keys.
     * More readable than 2D array when you have many columns.
     * <p>
     * Returns:
     * [
     * {username=standard_user, password=secret_sauce, expected=success},
     * {username=locked_out, password=secret_sauce, expected=locked_error}
     * ]
     * <p>
     * Usage in test: map.get("username") instead of data[0]
     */
    public static List<Map<String, String>> getExcelDataAsMap(String filePath, String sheetName) {
        try (
                FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)
        ) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            List<Map<String, String>> dataList = new ArrayList<>();

            // Read header row to get column names
            Row headerRow = sheet.getRow(0);
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue().trim());
            }

            // Read data rows
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                Map<String, String> rowMap = new LinkedHashMap<>();

                for (int j = 0; j < headers.size(); j++) {
                    rowMap.put(headers.get(j), getCellValueAsString(row.getCell(j)));
                }

                dataList.add(rowMap);
            }
            log.info("Read {} rows as maps from sheet '{}'", dataList.size(), sheetName);

            return dataList;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
    }

    /**
     * Converts any cell type to String safely.
     * Excel stores numbers as NUMERIC type, booleans as BOOLEAN, etc.
     * We convert everything to String for uniform handling.
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                // Check if it's a date
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toString();
                }

                // Avoid "1.0" for integer values — return "1" instead
                double value = cell.getNumericCellValue();
                if (value == Math.floor(value)) {
                    yield String.valueOf((long) value);
                }
                yield String.valueOf(value);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getStringCellValue().trim();
            case BLANK -> "";
            default -> "";
        };
    }

}
