package ru.clevertec.util;

import ru.clevertec.annotation.JsonField;

import java.lang.reflect.Field;

public class JsonFieldMapper {

    public static Field findFieldByJsonName(Class<?> clazz, String jsonFieldName) {
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            if (field.isAnnotationPresent(JsonField.class)) {
                fieldName = field.getAnnotation(JsonField.class).value();
            }
            if (fieldName.equals(jsonFieldName)) {
                return field;
            }
        }
        return null;
    }

    public static void setFieldValue(Object object, Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(object, value);
        field.setAccessible(false);
    }
}