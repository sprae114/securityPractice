package com.example.ch01basictest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity // 스프링 시큐리티가 제공하는 필터들이 자동으로 포함된다.
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션을 메소드 단위로 사용할 수 있게 한다.
class SecurityConfig{

    /**
     * SecurityFilterChain 을 빈으로 등록하면, 필터 체인 설정을 할 수 있다.
     * @param http : HttpSecurity 객체를 파라미터로 받는다.
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .and()
                .authorizeRequests(auth->{
                    auth.anyRequest().permitAll();
                });

        return http.build();
    }

    /**
     * UserDetailsService 를 빈으로 등록하면, 인메모리 유저를 설정할 수 있다.
     */
    @Bean
    UserDetailsService userService(){
        final PasswordEncoder pw = passwordEncoder();
        UserDetails user2 = User.builder()
                .username("user2")
                .password(pw.encode("1234"))
                .roles("USER").build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(pw.encode("1234"))
                .roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user2, admin);
    }

    /**
     * PasswordEncoder 를 빈으로 등록하면, 비밀번호를 암호화할 수 있다.
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}