package ru.clevertec.service;

import ru.clevertec.deserializer.DeserializerFunction;
import ru.clevertec.type_handler.JsonTypeHandler;
import ru.clevertec.type_handler.impl.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonConverter {

    private final List<JsonTypeHandler> handlers = List.of(
            new StringTypeHandler(),
            new IntegerTypeHandler(),
            new BooleanTypeHandler(),
            new ListTypeHandler()
    );

    public Optional<Object> convertValue(String value,
                                         Class<?> fieldType,
                                         Field field,
                                         DeserializerFunction deserializer) {

        if (value.equals("null")) {
            return Optional.empty();
        }

        return handlers.stream()
                .filter(handler -> handler.canHandle(fieldType, value))
                .findFirst()
                .or(() -> Optional.of(new JsonObjectHandler(deserializer)))
                .map(handler -> {
                    try {
                        return handler.handle(fieldType, value, field);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public List<Object> convertList(String value,
                                    Field field,
                                    DeserializerFunction deserializer) {
        validateJsonArray(value, field);

        String arrayContent = extractArrayContent(value);
        String[] items = splitArrayItems(arrayContent);

        Class<?> itemType = determineItemType(field);
        return convertItemsToList(items, itemType, field, deserializer);
    }

    private void validateJsonArray(String value, Field field) {
        if (!(value.startsWith("[") && value.endsWith("]"))) {
            throw new IllegalArgumentException("Expected JSON array for field: " + field.getName());
        }
    }

    private String extractArrayContent(String value) {
        return value.substring(1, value.length() - 1).trim();
    }

    private String[] splitArrayItems(String arrayContent) {
        JsonParserService jsonParserService = new JsonParserService();
        return jsonParserService.splitFields(arrayContent);
    }

    private Class<?> determineItemType(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
        } else {
            return Object.class;
        }
    }

    private List<Object> convertItemsToList(String[] items,
                                            Class<?> itemType,
                                            Field field,
                                            DeserializerFunction deserializer) {
        List<Object> list = new ArrayList<>();
        for (String item : items) {
            list.add(convertValue(item.trim(), itemType, field, deserializer).orElse(null));
        }
        return list;
    }



}
