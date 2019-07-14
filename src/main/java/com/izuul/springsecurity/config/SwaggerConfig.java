package com.izuul.springsecurity.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Guihong.Zhang
 * @date 2019-07-11 13:19
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String VERSION = "V1";

    /**
     * group :app
     * 扫描controller包生成API接口文档
     * @return
     */
//    @Bean
//    public Docket packageApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("JNJ api接口文档")
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.yidatec.jnj.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
    /**
     * group :rest 扫描注解了@ApiOperation的方法生成API接口文档
     *
     */
    @Bean
    public Docket RestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("JNJ api接口文档")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("JNJ RESTful APIs")
                .termsOfServiceUrl("")
                .description("JNJ api接口文档")
                .version(VERSION).build();
    }
}
