package com.it.uaa.web.context;

import com.it.uaa.common.infrastructure.ThreadLocalHolder;
import com.it.uaa.web.WebUtils;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  MkkCharacterEncodingFilter 类封装
 */
public class MkkCharacterEncodingFilter extends CharacterEncodingFilter {


    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        persistIp(request);
        super.doFilterInternal(request, response, filterChain);

    }

    private void persistIp(HttpServletRequest request) {
        final String clientIp = WebUtils.retrieveClientIp(request);
        ThreadLocalHolder.clientIp(clientIp);
    }


}