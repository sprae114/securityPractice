package com.example.ch03logincustomerfilter.student;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    private String id; // 학생 아이디
    private String username; // 학생 이름
    private Set<GrantedAuthority> role; // 권한 목록
}
