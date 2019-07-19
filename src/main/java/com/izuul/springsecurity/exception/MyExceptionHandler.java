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

    private static String PASSWORD_ERROR = "BadCredentialsException";
    private static String ACCOUNT_ERROR = "InternalAuthenticationServiceException";

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object myExceptionHandler(Exception e) {

        log.error("myExceptionHandler:{}", e.getMessage());
        System.err.println(e.getClass().getName());
        String exception = e.getClass().getName();
        // 密码错误
        if (exception.contains(PASSWORD_ERROR)) {
            log.info("密码错误");
            return new ResponseEntity<>(Result.builder()
                    .code(CodeEnum.PASSWORD_ERROR.getCode())
                    .message(CodeEnum.PASSWORD_ERROR.getMsg())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            // 账号不存在
        } else if (exception.contains(ACCOUNT_ERROR)) {
            log.info("账号不存在");
            return new ResponseEntity<>(Result.builder()
                    .code(CodeEnum.ACCOUNT_ERROR.getCode())
                    .message(CodeEnum.ACCOUNT_ERROR.getMsg())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SYSTEM_ERROR.getCode())
                .message(CodeEnum.SYSTEM_ERROR.getMsg())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
