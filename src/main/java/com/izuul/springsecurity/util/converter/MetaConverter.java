package com.izuul.springsecurity.util.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.izuul.springsecurity.entity.Meta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 20:46
 */
@Converter
@Slf4j
public class MetaConverter implements AttributeConverter<Meta, String> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Meta attribute) {
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
    public Meta convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, Meta.class);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("JSON 转 User 异常");
            return new Meta();
        }
    }
}
