package com.icinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.icinema.entity.User;


public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
