package com.bgi.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtil {

    public static String formatDate(String date) {
        if (null == date || "".equals(date.trim()) || "null".equals(date.trim())) {
            return null;
        }
        return date.split("T")[0];
    }

    public static String formatDateTime(String dateTime) {
        if (null == dateTime || "".equals(dateTime.trim()) || "null".equals(dateTime.trim())) {
            return null;
        }
        return new StringBuilder(dateTime).replace(10, 11, " ").substring(0, dateTime.length()-4);
    }

    public static LocalDateTime string2DateTime(String dateTime) {
        dateTime = formatDateTime(dateTime);
        if (null == dateTime) {
            return null;
        }
        return LocalDateTime.parse(dateTime);
    }

    public static LocalDate string2Date(String date) {
        date = formatDate(date);
        if (null == date) {
            return null;
        }
        return LocalDate.parse(date);
    }

}
