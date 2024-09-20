package ru.clevertec.service;

import ru.clevertec.annotation.JsonField;

import java.lang.reflect.Field;
import java.util.Optional;

public class JsonFieldMapper {

    public Optional<Field> findFieldByJsonName(Class<?> clazz, String jsonFieldName) {
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            if (field.isAnnotationPresent(JsonField.class)) {
                fieldName = field.getAnnotation(JsonField.class).value();
            }
            if (fieldName.equals(jsonFieldName)) {
                return Optional.of(field);
            }
        }
        return Optional.empty();
    }

    public void setFieldValue(Object object, Field field, Object value)
            throws IllegalAccessException {
        field.setAccessible(true);
        field.set(object, value);
        field.setAccessible(false);
    }
}
