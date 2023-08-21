package com.example.ch01basictest.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    /**
     * 모든 이용자의 정보를 나타냄 (로그인 유무 상관없이)
     *
     * @AuthenticationPrincipal : 현재 로그인한 사용자의 정보를 가져옴
     * @param user : 사용자의 정보
     */
    @GetMapping("/")
    public SecurityMessage hello(@AuthenticationPrincipal UserDetails user){
        return SecurityMessage.builder()
                .user(user)
                .message("hello")
                .build();
    }


    /**
     * ROLE_USER 권한을 가진 사용자만 접근 가능
     *
     * @param user : 사용자의 정보
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/user")
    public SecurityMessage user(@AuthenticationPrincipal UserDetails user){
        return SecurityMessage.builder()
                .user(user)
                .message("user page")
                .build();
    }

    /**
     * ROLE_ADMIN 권한을 가진 사용자만 접근 가능
     *
     * @param user : 사용자의 정보
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public SecurityMessage admin(@AuthenticationPrincipal UserDetails user){
        return SecurityMessage.builder()
                .user(user)
                .message("admin page")
                .build();
    }

}