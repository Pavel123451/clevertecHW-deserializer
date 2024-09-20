package ru.clevertec.type_handler.impl;

import ru.clevertec.type_handler.JsonTypeHandler;
import ru.clevertec.util.JsonUtil;

import java.lang.reflect.Field;

public class StringTypeHandler implements JsonTypeHandler {

    @Override
    public boolean canHandle(Class<?> fieldType, String value) {
        return fieldType == String.class;
    }

    @Override
    public Object handle(Class<?> fieldType, String value, Field field) {
        return JsonUtil.removeQuotes(value);
    }
}