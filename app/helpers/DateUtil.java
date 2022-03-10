package helpers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static String DATE_FORMAT = "MMM dd yyyy";

    public static LocalDate parseDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp * 1000L)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static String formatDateString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
