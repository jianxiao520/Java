package com.example.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

// 配合拦截器拦截返回信息
@RestControllerAdvice
public class MyExceptionHandler {

    /**
     * 拦截全局Runtime异常 -> 校验Token
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, Object>> exceptionHandler(Exception e) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 400);
        resultMap.put("message", e.getMessage());
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理Validated校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<Map<String, Object>> handleParameterVerificationException(Exception e) {
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("code", 400);
        String msg = null;
        /// BindException
        if (e instanceof BindException) {
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            FieldError fieldError = ((BindException) e).getFieldError();
            if (fieldError != null) {
                msg = fieldError.getDefaultMessage();
            }
        }
        resultMap.put("message", msg);
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.BAD_REQUEST);
    }
}