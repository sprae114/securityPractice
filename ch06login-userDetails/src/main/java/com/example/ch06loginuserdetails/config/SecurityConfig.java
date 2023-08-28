package com.example.ch06loginuserdetails.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@EnableWebSecurity // 웹 보안 활성화
@EnableGlobalMethodSecurity(prePostEnabled = true) // 메소드 보안 활성화 : @PreAuthorize 어노테이션을 메소드에 사용할 수 있게 함
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthDetail customAuthDetail;

    /**
     * http 요청에 대한 보안 설정
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 정적 리소스에 대한 요청은 모두 허용
                        .mvcMatchers("/", "/auth").permitAll()  // "/"에 대한 요청은 모두 허용
                        .anyRequest().authenticated())  // 그 외의 요청은 인증 필요
            .formLogin(login -> login
                    .loginPage("/login") // 로그인 페이지 주소
                    .loginProcessingUrl("/loginprocess").permitAll()  // 로그인 처리 URL : form 태그의 action 속성과 일치해야 함
                    .defaultSuccessUrl("/", false)  // 로그인 성공 후 리다이렉트 주소
                    .authenticationDetailsSource(customAuthDetail)  // 인증 정보를 저장하는 객체 설정 : 로그인 시간, IP 주소, 세션 ID 등
                    .failureUrl("/login-error"))  // 로그인 실패 후 주소
            .logout(logout -> logout
                    .logoutUrl("/logout")  // 로그아웃 주소
                    .logoutSuccessUrl("/")) // 로그아웃 성공 후 리다이렉트 주소
            .exceptionHandling(error->
                    error.accessDeniedPage("/access-denied")) // 접근 거부 시 리다이렉트 될 페이지
        ;
    }

    /**
     * 계정의 권한 계층 설정
     * admin 계정은 user 계정의 권한을 모두 가지고 있음
     */
    @Bean
    RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    /**
     * 로그인을 위한 계정 생성 및 권한 부여
     * @param auth : AuthenticationManagerBuilder 객체
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication() // 메모리에 계정 정보를 저장
                .withUser(User.withDefaultPasswordEncoder() // 유저 계정 생성
                    .username("user1")
                    .password("1111")
                    .roles("USER"))
                .withUser(User.withDefaultPasswordEncoder() // admin 계정 생성
                    .username("admin")
                    .password("2222")
                    .roles("ADMIN"));
    }

}
