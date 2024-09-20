package ru.clevertec.deserializer;

@FunctionalInterface
public interface DeserializerFunction {
    <T> T deserialize(String json, Class<T> clazz);
}
