package org.acme.events.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MapperFactory {
    private static final ObjectMapper objectMapper = new ObjectMapper()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .registerModule(new JavaTimeModule());

    public static ObjectMapper defaultMapper() {
        return objectMapper;
    }

    public static String serialize(Object payloadObject) throws JsonProcessingException {
        return payloadObject == null ? null : objectMapper.writeValueAsString(payloadObject);
    }

    public static <T> T deserialize(String json, Class<T> clazz) throws JsonProcessingException {
        return json == null ? null : objectMapper.readValue(json, clazz);
    }
}
