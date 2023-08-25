package com.example.ch03logincustomerfilter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    public CustomLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 인증 처리 요청
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request); // username을 가져옴
        username = (username != null) ? username.trim() : ""; // username이 null이 아니면 username을 trim한 값으로 초기화

        String password = obtainPassword(request); // password를 가져옴
        password = (password != null) ? password : ""; // password가 null이 아니면 password를 trim한 값으로 초기화

        String type = request.getParameter("type");

        if (type == null || type.equals("student")){ // 학생일 경우
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            return this.getAuthenticationManager().authenticate(authRequest);
        }

        if (type.equals("teacher")) {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            return this.getAuthenticationManager().authenticate(authRequest);
        }

        throw new AuthenticationServiceException("Authentication type not supported: " + type);
    }
}
