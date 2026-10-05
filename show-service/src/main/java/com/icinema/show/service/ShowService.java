package com.icinema.show.service;

import com.icinema.show.entity.Show;
import com.icinema.show.repository.ShowRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShowService {

    private final ShowRepository showRepository;

    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    public Show getShowById(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found"));
    }

    public Show createShow(Show show) {
        if (show.getStatus() == null || show.getStatus().isBlank()) {
            show.setStatus("ACTIVE");
        }

        return showRepository.save(show);
    }

    public Show updateShow(Long id, Show updatedShow) {
        Show existingShow = getShowById(id);

        existingShow.setMovieId(updatedShow.getMovieId());
        existingShow.setTheatreId(updatedShow.getTheatreId());
        existingShow.setScreenId(updatedShow.getScreenId());
        existingShow.setShowDate(updatedShow.getShowDate());
        existingShow.setStartTime(updatedShow.getStartTime());
        existingShow.setEndTime(updatedShow.getEndTime());
        existingShow.setTicketPrice(updatedShow.getTicketPrice());
        existingShow.setStatus(updatedShow.getStatus());

        return showRepository.save(existingShow);
    }

    public void deleteShow(Long id) {
        Show show = getShowById(id);
        showRepository.delete(show);
    }

    public List<Show> getShowsByMovie(Long movieId) {
        return showRepository.findByMovieId(movieId);
    }

    public List<Show> getShowsByTheatre(Long theatreId) {
        return showRepository.findByTheatreId(theatreId);
    }

    public List<Show> getShowsByScreen(Long screenId) {
        return showRepository.findByScreenId(screenId);
    }

    public List<Show> getShowsByMovieAndDate(Long movieId, LocalDate showDate) {
        return showRepository.findByMovieIdAndShowDate(movieId, showDate);
    }

    public List<Show> getShowsByScreenAndDate(Long screenId, LocalDate showDate) {
        return showRepository.findByScreenIdAndShowDate(screenId, showDate);
    }

    public List<Show> getActiveShows() {
        return showRepository.findByStatus("ACTIVE");
    }
}