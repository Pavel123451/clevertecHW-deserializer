package ru.clevertec.type_handler.impl;

import ru.clevertec.type_handler.JsonTypeHandler;

import java.lang.reflect.Field;

public class IntegerTypeHandler implements JsonTypeHandler {

    @Override
    public boolean canHandle(Class<?> fieldType, String value) {
        return Integer.TYPE.equals(fieldType);
    }

    @Override
    public Object handle(Class<?> fieldType, String value, Field field) {
        return Integer.parseInt(value);
    }
}

