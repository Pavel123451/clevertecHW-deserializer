package ru.clevertec.util;

import ru.clevertec.deserializer.DeserializerFunction;
import ru.clevertec.type_handler.JsonTypeHandler;
import ru.clevertec.type_handler.impl.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonConverter {

    private static final List<JsonTypeHandler> handlers = List.of(
            new StringTypeHandler(),
            new IntegerTypeHandler(),
            new BooleanTypeHandler(),
            new ListTypeHandler()
    );

    public static Object convertValue(
            String value,
            Class<?> fieldType,
            Field field,
            DeserializerFunction deserializer) throws Exception {

        if (value.equals("null")) {
            return null;
        }

    return handlers.stream()
            .filter(handler -> handler.canHandle(fieldType, value))
            .findFirst()
            .orElseGet(() -> new JsonTypeHandler() {
                @Override
                public boolean canHandle(Class<?> fieldType1, String value1) {
                    return value1.startsWith("{") && value1.endsWith("}");
                }

                @Override
                public Object handle(Class<?> fieldType1, String value1, Field field1) {
                    return deserializer.deserialize(value1, fieldType1);
                }
            })
            .handle(fieldType, value, field);
    }

    public static List<Object> convertList(String value, Field field, DeserializerFunction deserializer) throws Exception {
        if (!(value.startsWith("[") && value.endsWith("]"))) {
            throw new IllegalArgumentException("Expected JSON array for field: " + field.getName());
        }

        String arrayContent = value.substring(1, value.length() - 1).trim();
        String[] items = JsonParser.splitFields(arrayContent);

        List<Object> list = new ArrayList<>();
        Type genericType = field.getGenericType();
        Class<?> itemType;

        if (genericType instanceof ParameterizedType) {
            itemType = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
        } else {
            itemType = Object.class;
        }

        for (String item : items) {
            list.add(convertValue(item.trim(), itemType, field, deserializer));
        }

        return list;
    }
}
