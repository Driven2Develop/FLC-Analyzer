package helpers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Helper class for Date-related operations
 * @author Yvonne Lee
 */
public class DateUtil {

    public static String DATE_FORMAT = "dd/MM/yyyy";

    /**
     * Util method to parse epoch timestamp to LocalDate
     *
     * @param timestamp Epoch timestamp
     * @return Parsed LocalDate value
     * @author Yvonne Lee
     */
    public static LocalDate parseDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp * 1000L)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * Util method to format date to string value
     *
     * @param date Date to format
     * @return Formatted date value
     * @author Yvonne Lee
     */
    public static String formatDateString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
