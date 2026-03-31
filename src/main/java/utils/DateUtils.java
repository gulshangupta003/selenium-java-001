package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private DateUtils() {
    }

    /**
     * Returns current timestamp: "2025-03-28_14-30-45"
     * Safe for filenames (no colons or spaces).
     */
    public static String getTimestamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
    }

    /**
     * Returns readable date: "March 28, 2025 02:30 PM"
     * Used in report headers.
     */
    public static String getReadableDateTime() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a"));
    }

    /**
     * Returns date in custom format.
     * Example: getFormattedDate("dd/MM/yyyy") → "28/03/2025"
     */
    public static String getFormatedDate(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Returns current date in ISO format: "2025-03-28"
     * Common format for API payloads and database entries.
     */
    public static String getCurrentDate() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

}
