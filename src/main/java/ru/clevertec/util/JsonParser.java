package ru.clevertec.util;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static String[] splitFields(String jsonString) {
        List<String> fields = new ArrayList<>();
        int bracketDepth = 0;
        int lastSplit = 0;

        for (int i = 0; i < jsonString.length(); i++) {
            char c = jsonString.charAt(i);
            if (c == '{' || c == '[') {
                bracketDepth++;
            } else if (c == '}' || c == ']') {
                bracketDepth--;
            } else if (c == ',' && bracketDepth == 0) {
                fields.add(jsonString.substring(lastSplit, i).trim());
                lastSplit = i + 1;
            }
        }
        fields.add(jsonString.substring(lastSplit).trim());
        return fields.toArray(new String[0]);
    }

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
