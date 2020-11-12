package com.xueqiang.footmark.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static final ObjectMapper mapper = StaticObjectInit.JSON_OBJECT_MAPPER
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public static <T> String toJson(T obj) {
        String serialValue = null;
        try {
            serialValue = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("class:{} instance serial to Json has error:{}", obj.toString(), e.getMessage(), e);
        }
        return serialValue;
    }


    public static <T> T fromStr(String json, Class<T> clz) {
        T deSerialObj = null;
        try {
            deSerialObj = mapper.readValue(json, clz);
        } catch (IOException e) {
            logger.error("Class: {} deserialize from Json {} failed: {}", clz, json, e.getMessage(), e);
        }
        return deSerialObj;
    }

    public static <T> T fromStr(String json, TypeReference<T> valueTypeRef) {
        T deSerialObj = null;
        try {
            deSerialObj = mapper.readValue(json, valueTypeRef);
        } catch (Exception ex) {
            logger.error("TypeReference: {} deserialize from Json {} failed: {}", valueTypeRef, json, ex.getMessage(), ex);
        }
        return deSerialObj;
    }

    public static <T> T fromInputStream(InputStream inputStream, Class<T> clazz) throws IOException {
        if (inputStream == null) {
            return null;
        }

        return mapper.readValue(inputStream, clazz);
    }

    public static <T> T fromInputStream(InputStream inputStream, JavaType type) throws IOException {
        if (inputStream == null) {
            return null;
        }

        return mapper.readValue(inputStream, type);
    }

    public static <T> List<T> getListFormStr(String json, Class<T> clz) {
        return (List<T>) getCollectionFromStr(json, clz, List.class);
    }

    public static <T> Collection<T> getCollectionFromStr(String json, Class<T> originClass, Class<?> collectionClass) {
        JavaType javaType = mapper.getTypeFactory().constructParametrizedType(collectionClass, Collection.class, originClass);
        Collection<T> deSerialList = null;
        try {
            deSerialList = mapper.readValue(json, javaType);
        } catch (Exception e) {
            logger.error("Collection of class:{} deSerial from json:{} has error:{}", originClass, json, e.getMessage(), e);
        }
        return deSerialList;
    }

    public static <K, V> Map<K, V> getMapFromStr(String input) {
        TypeReference<Map<K, V>> mapTypeReference = new TypeReference<Map<K, V>>(){};
        return fromStr(input, mapTypeReference);
    }

    public static String textField(JsonNode node, String field) throws Exception {
        return textField(node, field, "");
    }

    public static String textField(JsonNode node, String field, String defaultValue) throws Exception {
        JsonNode jsonNode = node.get(field);
        if (jsonNode != null) {
            if (jsonNode.isNull()) {
                return defaultValue;
            }
            return jsonNode.asText(defaultValue);
        } else {
            logger.debug("textField error,field: {}", field);
            throw new IllegalArgumentException("not exist field :" + field);
        }
    }

    public static int intField(JsonNode node, String field, int defaultValue) throws Exception {
        JsonNode jsonNode = node.get(field);
        if (jsonNode != null) {
            if (jsonNode.isNull()) {
                return defaultValue;
            }
            return jsonNode.asInt(defaultValue);
        } else {
            logger.error("intField error,field: {}", field);
            throw new IllegalArgumentException("not exist field :" + field);
        }
    }
}
