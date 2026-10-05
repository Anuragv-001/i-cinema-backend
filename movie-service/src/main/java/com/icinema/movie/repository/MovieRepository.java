package com.icinema.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icinema.movie.entity.*;

public interface MovieRepository extends JpaRepository<Movie, Long>{
    
}
