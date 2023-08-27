package com.example.ch03logincustomerfilter.config;

import com.example.ch03logincustomerfilter.student.StudentManager;
import com.example.ch03logincustomerfilter.teacher.TeacherManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(1) // 순서를 지정 : 1이 가장 먼저 적용
@Configuration // 스프링 설정 클래스
@RequiredArgsConstructor
public class MobileSecurityConfig extends WebSecurityConfigurerAdapter{

    private final StudentManager studentManager;
    private final TeacherManager teacherManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .antMatcher("/api/**")  // /api/** 패턴의 URL에 대해서만 적용
                .csrf().disable()
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .httpBasic();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(studentManager);
        auth.authenticationProvider(teacherManager);
    }
}
