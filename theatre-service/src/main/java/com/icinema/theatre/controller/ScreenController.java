package com.icinema.theatre.controller;

import com.icinema.theatre.entity.Screen;
import com.icinema.theatre.service.ScreenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
// @CrossOrigin("http://localhost:5173")
public class ScreenController {

    private final ScreenService screenService;

    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @GetMapping
    public ResponseEntity<List<Screen>> getAllScreens() {
        return ResponseEntity.ok(screenService.getAllScreens());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScreenById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(screenService.getScreenById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<List<Screen>> getScreensByTheatreId(
            @PathVariable Long theatreId) {
        return ResponseEntity.ok(
                screenService.getScreensByTheatreId(theatreId)
        );
    }

    @PostMapping
    public ResponseEntity<Screen> createScreen(@RequestBody Screen screen) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(screenService.createScreen(screen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateScreen(
            @PathVariable Long id,
            @RequestBody Screen screen) {
        try {
            return ResponseEntity.ok(
                    screenService.updateScreen(id, screen)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteScreen(@PathVariable Long id) {
        try {
            screenService.deleteScreen(id);
            return ResponseEntity.ok("Screen deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}