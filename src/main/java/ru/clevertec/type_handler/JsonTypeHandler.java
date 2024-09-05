package ru.clevertec.type_handler;

import java.lang.reflect.Field;

public interface JsonTypeHandler {
    boolean canHandle(Class<?> fieldType, String value);

    Object handle(Class<?> fieldType, String value, Field field) throws Exception;
}
