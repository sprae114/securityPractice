package com.example.commonstudent.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String main(Model model, HttpSession session){
        model.addAttribute("sessionId", session.getId());
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }

    @GetMapping("/login-required")
    public String loginRequired(){
        return "LoginRequired";
    }


    @GetMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError", true);
        return "loginForm";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "AccessDenied";
    }

    @GetMapping("/access-denied2")
    public String accessDenied2(){
        return "AccessDenied2";
    }

    @ResponseBody // Authentication 객체를 JSON 형태로 반환
    @GetMapping("/auth")
    public Authentication auth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")  // ROLE_USER 권한이 있어야만 접근 가능
    @GetMapping("/user-page")
    public String userPage() throws YouCannotAccessUserPage {
        return "UserPage";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // ROLE_ADMIN 권한이 있어야만 접근 가능
    @GetMapping("/admin-page")
    public String adminPage(){
        return "AdminPage";
    }

}
