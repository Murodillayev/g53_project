package uz.pdp.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Utils {
    public static String dateFormat(String createdAt, String pattern) {
        try {
            // ISO-8601 formatidagi createdAt ni parse qilish
            LocalDateTime localDateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            // Kerakli pattern bilan formatlash
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return localDateTime.format(formatter);
        } catch (DateTimeParseException e) {
            return "Invalid date format: " + createdAt + " does not match expected ISO format";
        }
    }
}
