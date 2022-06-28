package com.pinakibarik.coinexchangeapi.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import java.util.Map;

public class JsonUtility {
    public static final <T extends Object> T deserialize(String json, Class<T> clazz) throws UnrecognizedPropertyException, InvalidFormatException {
        return deserialize(json, clazz, null);
    }

    public static final <T extends Object> T deserialize(String json, Class<T> clazz, Map<DeserializationFeature, Boolean> features) throws UnrecognizedPropertyException, InvalidFormatException {
        if (json == null)
            return null;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        if (features != null && !features.isEmpty()) {
            features.keySet().forEach(
                    key -> {
                        objectMapper.configure(key, features.get(key));
                    }
            );
        }

        try {
            return clazz.cast(objectMapper.readValue(json, clazz));
        } catch (InvalidFormatException | UnrecognizedPropertyException ex) {
            throw ex;
        } catch (ClassCastException ex) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    public static final String serialize(Object object) {
        return serialize(object, null);
    }

    public static final String serialize(Object object, Map<SerializationFeature, Boolean> features) {
        if (object == null)
            return "{}";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        if (features != null && !features.isEmpty()) {
            features.keySet().forEach(
                    key -> {
                        objectMapper.configure(key, features.get(key));
                    }
            );
        }

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            return "{}";
        }
    }
}
