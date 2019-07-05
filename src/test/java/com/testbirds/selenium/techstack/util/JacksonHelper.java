package com.testbirds.selenium.techstack.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonHelper {

    private static final JacksonHelper inst = new JacksonHelper();

    private final ObjectMapper mapper;

    private JacksonHelper() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static final ObjectMapper getMapper() {
        return inst.mapper;
    }
}
