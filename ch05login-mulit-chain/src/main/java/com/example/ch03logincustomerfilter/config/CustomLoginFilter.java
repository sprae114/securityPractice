package com.example.ch03logincustomerfilter.config;

import com.example.ch03logincustomerfilter.student.StudentAuthenticationToken;
import com.example.ch03logincustomerfilter.teacher.TeacherAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
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

        if (type == null || !type.equals("teacher")){ // 선생님일 경우
            StudentAuthenticationToken authRequest = StudentAuthenticationToken.builder() // 학생 인증 토큰 생성
                    .credentials(username) // username을 credentials로 설정 : 비밀번호가 없으므로 이름으로 인증 확인
                    .build();

            return this.getAuthenticationManager().authenticate(authRequest); // 인증 처리
        }

        else { // 학생일 경우
            TeacherAuthenticationToken authRequest = TeacherAuthenticationToken.builder()  // 선생님 인증 토큰 생성
                    .credentials(username) // username을 credentials로 설정 : 비밀번호가 없으므로 이름으로 인증 확인
                    .build();

            return this.getAuthenticationManager().authenticate(authRequest); // 인증 처리
        }
    }
}
