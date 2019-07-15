package com.izuul.springsecurity.exception;

import com.izuul.springsecurity.controller.vo.CodeEnum;
import com.izuul.springsecurity.controller.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 09:47
 */
@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object myExceptionHandler(Exception e) {

        log.error("myExceptionHandler:{}", e.getMessage());
        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .message(CodeEnum.SUCCESS.getMsg())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
