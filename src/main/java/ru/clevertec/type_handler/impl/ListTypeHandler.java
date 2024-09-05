package ru.clevertec.type_handler.impl;

import ru.clevertec.deserializer.JsonDeserializer;
import ru.clevertec.type_handler.JsonTypeHandler;
import ru.clevertec.util.JsonConverter;

import java.lang.reflect.Field;
import java.util.List;

public class ListTypeHandler implements JsonTypeHandler {

    @Override
    public boolean canHandle(Class<?> fieldType, String value) {
        return fieldType.isArray() || List.class.isAssignableFrom(fieldType);
    }

    @Override
    public Object handle(Class<?> fieldType, String value, Field field) throws Exception {
        return JsonConverter.convertList(value, field, JsonDeserializer::deserialize);
    }
}
