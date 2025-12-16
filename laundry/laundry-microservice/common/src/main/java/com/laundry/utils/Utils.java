package com.laundry.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

@Slf4j
public class Utils {
    private static final ObjectMapper objectMapper = initObjectMapper();
    private static final String NUMBER = "0123456789";
    public static String convertObjectToJson(Object o) {
        try {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("convertObjectToJson error: {}", e.getMessage());
        }
        return null;
    }

    public static <T> T convertJsonToObject(String Json, Class<T> typeParameterClass ) {
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(Json, typeParameterClass);
        } catch (JsonProcessingException e) {
            log.error("");
        }
        return null;

    }
    public static ObjectMapper initObjectMapper() {
        return new ObjectMapper().findAndRegisterModules()
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
    }

    public static String generateRandomNumber(int length) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(NUMBER.charAt(rnd.nextInt(NUMBER.length())));
        }
        return sb.toString();
    }
}
