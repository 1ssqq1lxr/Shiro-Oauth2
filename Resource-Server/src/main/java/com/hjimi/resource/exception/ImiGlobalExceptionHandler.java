package com.hjimi.resource.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 17:36
 * @Description:
 */

@CrossOrigin
@ControllerAdvice
@Slf4j
public class ImiGlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity<Map<String,Object>> processException(Exception ex){
        if( ex instanceof UnauthorizedException ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        else if(ex instanceof UnauthenticatedException){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else if(ex instanceof OAuthProblemException){
            OAuthProblemException oAuthProblemException =(OAuthProblemException)ex;
            Map<String,Object> map = new HashMap<>();
            map.put("error",oAuthProblemException.getError());
            map.put("error_desc",oAuthProblemException.getDescription());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(map);
        }
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .build();
    }



}
