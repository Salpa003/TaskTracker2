package util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TimeFormatter {

    private static String formatInterval = "%d seconds";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String durationToSql(Duration duration) {
        return String.format(formatInterval, duration.toSeconds());
    }

    public static String timestamp(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
