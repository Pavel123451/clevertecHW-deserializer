package ru.clevertec.deserializer;

import ru.clevertec.service.JsonConverter;
import ru.clevertec.service.JsonFieldMapper;
import ru.clevertec.service.JsonParserService;
import ru.clevertec.util.JsonUtil;

import java.lang.reflect.Field;
import java.util.Optional;

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
        String[] fields = extractFields(jsonString);

        for (String field : fields) {
            processField(field, clazz, instance);
        }

        return instance;
    }

    private static String[] extractFields(String jsonString) {
        JsonParserService parserService = new JsonParserService();
        return parserService.splitFields(jsonString);
    }

    private static <T> void processField(String field, Class<T> clazz, T instance) {
        String[] keyValue = JsonUtil.splitKeyValue(field);
        if (keyValue.length < 2) {
            return;
        }

        String fieldName = JsonUtil.removeQuotes(keyValue[0].trim());
        String value = keyValue[1].trim();

        Optional<Field> classFieldOpt = findClassField(clazz, fieldName);
        if (classFieldOpt.isPresent()) {
            Field classField = classFieldOpt.get();
            Optional<?> convertedValue = convertFieldValue(value, classField);
            setFieldValue(instance, classField, convertedValue);
        }
    }

    private static Optional<Field> findClassField(Class<?> clazz, String fieldName) {
        JsonFieldMapper fieldMapper = new JsonFieldMapper();
        return fieldMapper.findFieldByJsonName(clazz, fieldName);
    }

    private static Optional<?> convertFieldValue(String value, Field classField) {
        JsonConverter converter = new JsonConverter();
        return converter.convertValue(value,
                classField.getType(),
                classField,
                JsonDeserializer::deserialize);
    }

    private static <T> void setFieldValue(T instance,
                                          Field classField,
                                          Optional<?> convertedValue) {
        convertedValue.ifPresent(value -> {
            try {
                JsonFieldMapper jsonFieldMapper = new JsonFieldMapper();
                jsonFieldMapper.setFieldValue(instance, classField, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to set field value", e);
            }
        });
    }
}
