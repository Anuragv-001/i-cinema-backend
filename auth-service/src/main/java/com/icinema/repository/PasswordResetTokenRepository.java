package com.icinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icinema.entity.PasswordResetToken;
import java.util.Optional;


public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
    Optional<PasswordResetToken> findTopByEmailOrderByIdDesc(String email);
}
