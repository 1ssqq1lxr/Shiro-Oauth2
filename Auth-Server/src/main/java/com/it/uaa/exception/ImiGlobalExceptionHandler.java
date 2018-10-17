package com.it.uaa.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 17:36
 * @Description: 全局异常处理器
 */

@CrossOrigin
@RestControllerAdvice
@Slf4j
public class ImiGlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public void processException(Exception ex){
            log.info("error",ex);

    }



}
