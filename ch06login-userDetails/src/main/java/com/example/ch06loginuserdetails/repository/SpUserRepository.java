package com.example.ch06loginuserdetails.repository;

import com.example.ch06loginuserdetails.domain.SpUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpUserRepository extends JpaRepository<SpUser, Long> {

    Optional<SpUser> findUserByEmail(String email);
}
