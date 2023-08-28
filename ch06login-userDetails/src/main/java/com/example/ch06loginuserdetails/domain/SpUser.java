package com.example.ch06loginuserdetails.domain;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보를 저장하는 클래스
 * 로그인 시 사용자 정보를 담고 있는 객체
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="sp_user")
public class SpUser implements UserDetails { // UserDetails 인터페이스를 구현하여 사용자 정보를 담고 있는 객체로 사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private String password;

    // 사용자 권한 정보를 담고 있는 객체를 EAGER로 설정하여 사용자 정보를 조회할 때마다 권한 정보를 같이 조회
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name="user_id")) // SpAuthority 클래스의 userId와 매핑
    private Set<SpAuthority> authorities;

    private boolean enabled;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { // 계정이 만료되지 않았는 지를 리턴
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() { // 계정이 잠기지 않았는 지를 리턴
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 계정의 비밀번호가 만료되지 않았는 지를 리턴
        return enabled;
    }

}