package com.icinema.show.repository;

import com.icinema.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieId(Long movieId);

    List<Show> findByTheatreId(Long theatreId);

    List<Show> findByScreenId(Long screenId);

    List<Show> findByMovieIdAndShowDate(Long movieId, LocalDate showDate);

    List<Show> findByScreenIdAndShowDate(Long screenId, LocalDate showDate);

    List<Show> findByStatus(String status);
}