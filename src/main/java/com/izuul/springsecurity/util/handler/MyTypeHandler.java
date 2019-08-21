package com.izuul.springsecurity.util.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: Guihong.Zhang
 * @date: 2019-08-21 13:52
 **/
@Slf4j
public class MyTypeHandler<T extends Object> extends BaseTypeHandler<T> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Class<T> clazz;

    static {
        MAPPER.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, false);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public MyTypeHandler(Class<T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("Type argument cannot be null");
        }
        this.clazz = clazz;
    }

    /**
     * object转json string
     *
     * @param object
     * @return
     */
    private String toJSON(T object) {
        try {
            String string = MAPPER.writeValueAsString(object);
            log.info("json handler string:{}", string);
            return string;
        } catch (Exception e) {
            log.error("convert json string to object failed, error message: {}", e.getMessage());
        }
        return null;
    }

    /**
     * json转object
     *
     * @param json
     * @param clazz
     * @return
     * @throws IOException
     */
    private T toObject(String json, Class<T> clazz) throws IOException {
        if (json != null && json != "") {
            return MAPPER.readValue(json, clazz);
        }
        return null;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setString(i, toJSON(t));
        } catch (Exception e) {
            log.error("preparedStatement set string failed, error message:{}", e.getMessage());
        }
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
        try {
            return toObject(resultSet.getString(s), clazz);
        } catch (IOException e) {
            log.error("convert json string to object failed, error message:{}", e.getMessage());
        }
        return null;
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        try {
            return toObject(resultSet.getString(i), clazz);
        } catch (IOException e) {
            log.error("convert json string to object failed, error message:{}", e.getMessage());
        }
        return null;
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        try {
            return toObject(callableStatement.getString(i), clazz);
        } catch (IOException e) {
            log.error("convert json string to object failed, error message:{}", e.getMessage());
        }
        return null;
    }
}