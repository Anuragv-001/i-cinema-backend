package com.icinema.theatre.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icinema.theatre.entity.Theatre;


public interface TheatreRepository extends JpaRepository<Theatre, Long>{
    
}
