package com.izuul.springsecurity.util.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 20:46
 */
@Converter
@Slf4j
public class ChildrenConverter implements AttributeConverter<List, String> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List attribute) {
        if (attribute != null) {
            try {
                return objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                log.error("Meta 转化 JSON 异常");
                return "";
            }
        }
        return "";
    }

    @Override
    public List convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return new ArrayList();
        }
        try {
            return objectMapper.readValue(dbData, List.class);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("JSON 转 User 异常");
            return new ArrayList();
        }
    }
}
