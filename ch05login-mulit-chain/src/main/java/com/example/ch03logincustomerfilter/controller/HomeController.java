package com.example.ch03logincustomerfilter.controller;


import com.example.ch03logincustomerfilter.student.Student;
import com.example.ch03logincustomerfilter.student.StudentManager;
import com.example.ch03logincustomerfilter.teacher.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    StudentManager studentManager;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }

    @GetMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError", true);
        return "loginForm";
    }

    @ResponseBody
    @GetMapping("/auth")
    public Authentication auth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }


    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT')")
    @GetMapping("/student/main")
    public String studentMain(){
        return "StudentMain";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/teacher/main")
    public String teacherMain(@AuthenticationPrincipal Teacher teacher, Model model){
        model.addAttribute("studentList", studentManager.myStudents(teacher.getId()));
        return "TeacherMain";
    }


    @ResponseBody // JSON 형태로 응답
    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/api/teacher/students")
    public List<Student> studentList(@AuthenticationPrincipal Teacher teacher){
        return studentManager.myStudents(teacher.getId());
    }


    @GetMapping("/access-denied")
    public String accessDenied(){
        return "AccessDenied";
    }
}
