package ru.clevertec.util;

public class JsonUtil {

    public static String[] splitKeyValue(String field) {
        return field.split(":", 2);
    }

    public static String removeQuotes(String str) {
        if (str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }
}
