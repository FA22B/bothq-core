package com.bothq.core.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ComponentSerializer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Serializes an object to a JSON string, including its class type information when necessary.
     *
     * @param value the object to serialize
     * @return a JSON string representation of the object
     * @throws JsonProcessingException if serialization fails
     */
    public static String serialize(Object value) throws JsonProcessingException {
        if (isSimpleType(value)) {
            return value.toString();
        } else {
            return objectMapper.writeValueAsString(value);
        }
    }

    /**
     * Deserializes a JSON string into an object of the specified class type.
     *
     * @param json     the JSON string to deserialize
     * @param dataType the class name of the target type
     * @return the deserialized object
     * @throws JsonProcessingException if deserialization fails
     * @throws ClassNotFoundException  if the class type is not found
     */
    public static Object deserialize(String json, String dataType) throws JsonProcessingException, ClassNotFoundException {
        Class<?> clazz = Class.forName(dataType);
        if (isSimpleType(clazz)) {
            return parseSimpleType(json, clazz);
        } else {
            return objectMapper.readValue(json, clazz);
        }
    }

    /**
     * Checks if the given object is of a simple type (e.g., String, Integer, etc.).
     *
     * @param value the object to check
     * @return true if it is a simple type, false otherwise
     */
    private static boolean isSimpleType(Object value) {
        return value instanceof Integer || value instanceof Boolean || value instanceof Double ||
                value instanceof Float || value instanceof Long || value instanceof Short ||
                value instanceof Byte || value instanceof Character || value instanceof String;
    }

    /**
     * Checks if the given class is a simple type.
     *
     * @param clazz the class to check
     * @return true if it is a simple type, false otherwise
     */
    private static boolean isSimpleType(Class<?> clazz) {
        return clazz.equals(Integer.class) || clazz.equals(Boolean.class) || clazz.equals(Double.class) ||
                clazz.equals(Float.class) || clazz.equals(Long.class) || clazz.equals(Short.class) ||
                clazz.equals(Byte.class) || clazz.equals(Character.class) || clazz.equals(String.class);
    }

    /**
     * Parses a simple type from a string.
     *
     * @param value the string to parse
     * @param clazz the class of the type to parse into
     * @return the parsed object
     */
    private static Object parseSimpleType(String value, Class<?> clazz) {
        if (clazz.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (clazz.equals(Boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (clazz.equals(Double.class)) {
            return Double.parseDouble(value);
        } else if (clazz.equals(Float.class)) {
            return Float.parseFloat(value);
        } else if (clazz.equals(Long.class)) {
            return Long.parseLong(value);
        } else if (clazz.equals(Short.class)) {
            return Short.parseShort(value);
        } else if (clazz.equals(Byte.class)) {
            return Byte.parseByte(value);
        } else if (clazz.equals(Character.class)) {
            return value.charAt(0);
        } else {
            return value;
        }
    }
}
