//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.aii.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.io.IOException;
import java.util.Collections;

@MappedTypes({Object.class})
@MappedJdbcTypes({JdbcType.ARRAY})
public class MybatisJSONTypeHandler extends AbstractJsonTypeHandler<Object> {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private final Class<?> type;

    public MybatisJSONTypeHandler(Class<?> type) {
        this.type = type;
    }

    protected Object parse(String json) {
        if ("[null]".equals(json)) {
            return Collections.EMPTY_LIST;
        }
        try {
            return objectMapper.readValue(json, this.type);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    protected String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        MybatisJSONTypeHandler.objectMapper = objectMapper;
    }
}
