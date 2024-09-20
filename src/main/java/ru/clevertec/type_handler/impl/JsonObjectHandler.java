package ru.clevertec.type_handler.impl;

import ru.clevertec.deserializer.DeserializerFunction;
import ru.clevertec.type_handler.JsonTypeHandler;

import java.lang.reflect.Field;

public class JsonObjectHandler implements JsonTypeHandler {
    private final DeserializerFunction deserializer;

    public JsonObjectHandler(DeserializerFunction deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public boolean canHandle(Class<?> fieldType, String value) {
        return value.startsWith("{") && value.endsWith("}");
    }

    @Override
    public Object handle(Class<?> fieldType, String value, Field field) {
        return deserializer.deserialize(value, fieldType);
    }
}
