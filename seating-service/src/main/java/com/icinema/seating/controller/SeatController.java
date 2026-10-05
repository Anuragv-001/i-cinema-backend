package com.icinema.seating.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.icinema.seating.entity.Seat;

import java.util.List;

import org.springframework.web.bind.annotation.PutMapping;

import com.icinema.seating.service.SeatService;


@RestController 
@RequestMapping("/api/seats")
// @CrossOrigin ("http://localhost:5173")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping
    public ResponseEntity<List<Seat>> getAllSeats(){
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSeatById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(seatService.getSeatById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<List<Seat>> getSeatsByScreenId(@PathVariable Long screenId){
        return ResponseEntity.ok(seatService.getSeatsByScreenId(screenId));
    }

    @PostMapping
    public ResponseEntity<Seat> createSeat(@RequestBody Seat seat){
        return ResponseEntity.status(HttpStatus.CREATED).body(seatService.createSeat(seat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSeat(@PathVariable Long id, @RequestBody Seat seat){
        try{
            return ResponseEntity.ok(seatService.updateSeat(id, seat));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSeat(@PathVariable Long id){
        try{
            seatService.deleteSeat(id);
            return ResponseEntity.ok("Seat Deleted Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<List<Seat>> generateSeats(
            @RequestParam Long screenId,
            @RequestParam int totalSeats,
            @RequestParam double price) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(seatService.generateSeats(screenId, totalSeats, price));
    }

    @PutMapping("/screen/{screenId}/resize")
    public ResponseEntity<?> resizeSeats(
            @PathVariable Long screenId,
            @RequestParam int totalSeats,
            @RequestParam double price) {

        try {
            return ResponseEntity.ok(
                    seatService.resizeSeats(
                            screenId,
                            totalSeats,
                            price
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}
