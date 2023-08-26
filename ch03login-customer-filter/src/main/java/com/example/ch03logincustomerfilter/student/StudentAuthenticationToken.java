package com.example.ch03logincustomerfilter.student;

import com.example.ch03logincustomerfilter.student.Student;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Student의 인증 정보를 담고 있음. (입장권)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAuthenticationToken implements Authentication {

    private Student principal; // Student 객체 : 인증 주체
    private String credentials; // 인증 확인을 위한 정보. 주로 비밀번호를 사용함.

    private String details; // 기타 인증요청에서 사용했던 정보들

    private boolean authenticated; // 인증 여부

    private Set<GrantedAuthority> authorities; // 권한 목록

    @Override
    public String getName() {
        return principal == null ? "" : principal.getUsername();
    }
}
