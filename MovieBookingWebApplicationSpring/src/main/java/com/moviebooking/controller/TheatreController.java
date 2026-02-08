package com.moviebooking.controller;

import com.moviebooking.entity.Theatre;
import com.moviebooking.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.moviebooking.dto.SeatUpdateRequest;

import java.util.List;
@RestController
@RequestMapping("/api/theatres")
@CrossOrigin(origins = "*")
public class TheatreController {

    @Autowired
    private TheatreService theatreService;

    @GetMapping
    public ResponseEntity<List<Theatre>> getAllTheatres() {
        return ResponseEntity.ok(theatreService.getAllTheatres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theatre> getTheatreById(@PathVariable int id) {
        Theatre theatre = theatreService.getTheatreById(id);
        if (theatre == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(theatre);
    }

    @PostMapping
    public ResponseEntity<Theatre> addTheatre(@RequestBody Theatre theatre) {
        Theatre saved = theatreService.addTheatre(theatre);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}/seats")
    public ResponseEntity<Theatre> updateSeats(
            @PathVariable int id,
            @RequestBody SeatUpdateRequest request) {

        Theatre updated = theatreService.updateSeats(id, request.getSeats());
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheatre(@PathVariable int id) {
        if (!theatreService.exists(id)) {
            return ResponseEntity.notFound().build();
        }
        theatreService.deleteTheatre(id);
        return ResponseEntity.noContent().build();
    }
}
