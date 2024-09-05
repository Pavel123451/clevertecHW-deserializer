package ru.clevertec.type_handler.impl;

import ru.clevertec.type_handler.JsonTypeHandler;

import java.lang.reflect.Field;

public class BooleanTypeHandler implements JsonTypeHandler {

    @Override
    public boolean canHandle(Class<?> fieldType, String value) {
        return fieldType == boolean.class || fieldType == Boolean.class;
    }

    @Override
    public Object handle(Class<?> fieldType, String value, Field field) {
        return Boolean.parseBoolean(value);
    }
}
