package com.example.ch03logincustomerfilter.teacher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component
public class TeacherManager implements AuthenticationProvider, InitializingBean {

    public final HashMap<String, Teacher> teacherMap = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TeacherAuthenticationToken token = (TeacherAuthenticationToken) authentication;

        if (teacherMap.containsKey(token.getCredentials())) {

            Teacher teacher = teacherMap.get(token.getCredentials());

            return TeacherAuthenticationToken.builder()
                    .principal(teacher)
                    .details(teacher.getUsername())
                    .authenticated(true)
                    .build();
        }

        return null;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == TeacherAuthenticationToken.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Teacher("choi", "최선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
        ).forEach(s -> teacherMap.put(s.getId(), s));
    }
}