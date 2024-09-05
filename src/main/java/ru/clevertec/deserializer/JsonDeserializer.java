package ru.clevertec.deserializer;

import ru.clevertec.util.JsonConverter;
import ru.clevertec.util.JsonParser;
import ru.clevertec.util.JsonFieldMapper;

import java.lang.reflect.Field;

public class JsonDeserializer {

    public static <T> T deserialize(String jsonString, Class<T> clazz) {
        try {
            jsonString = jsonString.trim();
            if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
                jsonString = jsonString.substring(1, jsonString.length() - 1).trim();
                return deserializeObject(jsonString, clazz);
            } else {
                throw new IllegalArgumentException("Invalid JSON object");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON", e);
        }
    }

    private static <T> T deserializeObject(String jsonString, Class<T> clazz) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();
        String[] fields = JsonParser.splitFields(jsonString);

        for (String field : fields) {
            String[] keyValue = JsonParser.splitKeyValue(field);
            String fieldName = JsonParser.removeQuotes(keyValue[0].trim());
            String value = keyValue[1].trim();

            Field classField = JsonFieldMapper.findFieldByJsonName(clazz, fieldName);
            if (classField != null) {
                Object convertedValue = JsonConverter.convertValue(
                        value,
                        classField.getType(),
                        classField,
                        JsonDeserializer::deserialize);
                JsonFieldMapper.setFieldValue(instance, classField, convertedValue);
            }
        }

        return instance;
    }
}
