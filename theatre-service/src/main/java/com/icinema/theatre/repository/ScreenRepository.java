package com.icinema.theatre.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icinema.theatre.entity.Screen;
public interface ScreenRepository extends JpaRepository<Screen, Long> {
    
}
