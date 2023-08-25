package com.example.ch03logincustomerfilter.student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

/**
 * 입장권 발급하는 역할
 */
@Component // 스프링 빈으로 등록
public class StudentManager implements AuthenticationProvider, InitializingBean {

    private HashMap<String, Student> studentDB = new HashMap<>(); // 학생 정보를 담을 DB


    /**
     * 인증 처리
     * @param authentication : 인증 객체
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken studentToken = (UsernamePasswordAuthenticationToken) authentication;

        if (studentDB.containsKey(studentToken.getName())) { // 학생 아이디가 존재한다면
            Student student = studentDB.get(studentToken.getName());

            return StudentAuthenticationToken.builder() // 학생 인증 토큰 생성
                    .principal(student)
                    .credentials(student.getId())
                    .details(student.getUsername())
                    .authenticated(true)
                    .build();
        }

        return null; // 인증 실패할 경우 null 반환
    }

    /**
     * UsernamePasswordAuthenticationToken을 지원하는지 확인
     * @param authentication : 인증 객체
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }

    /**
     * 학생 정보 초기화 (학생 정보를 DB에 저장)
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Student("hong", "홍길동", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT"))),
                new Student("kang", "강아지", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT"))),
                new Student("rang", "호랑이", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")))
        ).forEach(s->studentDB.put(s.getId(), s));
    }

}
