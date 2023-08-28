package com.example.ch03logincustomerfilter.config;

import com.example.ch03logincustomerfilter.student.StudentManager;
import com.example.ch03logincustomerfilter.teacher.TeacherManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;

import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Order(1)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final StudentManager studentManager;
    private final TeacherManager teacherManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .mvcMatchers("/", "/auth").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                    .loginPage("/login")
                    .loginProcessingUrl("/login").permitAll()
                    .defaultSuccessUrl("/", false)
                    .failureUrl("/login-error")
            )
            .addFilterAt(new CustomLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class) // CustomLoginFilter를 추가 : 로그인 요청을 처리하는 필터
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
            )
            .exceptionHandling(error -> error
                    .accessDeniedPage("/access-denied"));
    }


    @Bean
    protected RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_TEACHER > ROLE_STUDENT");
        return roleHierarchy;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(studentManager);
        auth.authenticationProvider(teacherManager);
    }
}
