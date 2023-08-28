package com.example.ch06loginuserdetails.config;

import com.example.ch06loginuserdetails.service.SpUserService;
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
    private final SpUserService spUserService;

    /**
     * http 요청에 대한 보안 설정
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()  // 정적 리소스에 대한 요청은 모두 허용
                            ,PathRequest.toH2Console()).permitAll()  // h2-console에 대한 요청은 모두 허용
                        .mvcMatchers("/", "/auth").permitAll()  // "/"에 대한 요청은 모두 허용
                        .anyRequest().authenticated())  // 그 외의 요청은 인증 필요
            .formLogin(login -> login
                    .loginPage("/login") // 로그인 페이지 주소
                    .loginProcessingUrl("/loginprocess").permitAll()  // 로그인 처리 URL : form 태그의 action 속성과 일치해야 함
                    .defaultSuccessUrl("/", false)  // 로그인 성공 후 리다이렉트 주소
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
     * 사용자 인증을 위한 UserDetailsService를 설정
     * @param auth : AuthenticationManagerBuilder 객체
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(spUserService);
    }

}
