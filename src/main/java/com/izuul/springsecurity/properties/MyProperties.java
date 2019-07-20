package com.izuul.springsecurity.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Guihong.Zhang
 * @date 2019-07-20 20:31
 */
@Data
@Component
@ConfigurationProperties(prefix = "properties")
public class MyProperties {
    private String test1;
}
