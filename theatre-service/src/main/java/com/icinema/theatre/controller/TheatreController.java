package com.icinema.theatre.controller;

import com.icinema.theatre.entity.Theatre;
import com.icinema.theatre.service.TheatreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
// @CrossOrigin("http://localhost:5173")
public class TheatreController {

    private final TheatreService theatreService;

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    @GetMapping
    public ResponseEntity<List<Theatre>> getAllTheatres() {
        return ResponseEntity.ok(theatreService.getAllTheatres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTheatreById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(theatreService.getTheatreById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Theatre> createTheatre(@RequestBody Theatre theatre) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(theatreService.createTheatre(theatre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTheatre(
            @PathVariable Long id,
            @RequestBody Theatre theatre) {
        try {
            return ResponseEntity.ok(
                    theatreService.updateTheatre(id, theatre)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTheatre(@PathVariable Long id) {
        try {
            theatreService.deleteTheatre(id);
            return ResponseEntity.ok("Theatre deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}