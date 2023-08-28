package com.example.ch06loginuserdetails.domain;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 사용자 권한 정보를 저장하는 클래스
 * 각각의 로그인된 유저에게 적절한 접근 권한을 부여하는 역할
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="sp_user_authority")
@IdClass(SpAuthority.class) // 복합키를 지정하기 위해 @IdClass 어노테이션을 사용
public class SpAuthority implements GrantedAuthority {

    @Id
    @Column(name="user_id")
    private Long userId;

    @Id
    private String authority;

}