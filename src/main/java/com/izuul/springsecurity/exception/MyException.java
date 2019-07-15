package com.izuul.springsecurity.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 09:47
 */
public class MyException extends Exception {

    private static final long serialVersionUID = 1926439629486459955L;

    private HttpStatus httpStatus;
    private String solution;

    /**
     *
     */
    public MyException() {

        super();
    }

    /**
     * @param message
     * @param cause
     */
    public MyException(String message, Throwable cause) {

        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param httpStatus
     */
    public MyException(String message, Throwable cause, HttpStatus httpStatus) {

        super(message, cause);
        this.httpStatus = httpStatus;
    }


    /**
     * @param message
     */
    public MyException(String message) {

        super(message);
    }

    /**
     * @param cause
     */
    public MyException(Throwable cause) {

        super(cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
