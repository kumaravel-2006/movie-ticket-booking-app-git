package com.moviebooking.service;

import com.moviebooking.entity.Theatre;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreService {

    @Autowired
    private TheatreRepository theatreRepository;

    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    public Theatre getTheatreById(int id) {
        return theatreRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Theatre not found with id: " + id));
    }

    public Theatre addTheatre(Theatre theatre) {
        return theatreRepository.save(theatre);
    }

    public Theatre updateSeats(int id, int seats) {
        Theatre theatre = getTheatreById(id);
        theatre.setAvailableSeats(seats);
        return theatreRepository.save(theatre);
    }

    public void deleteTheatre(int id) {
        Theatre theatre = getTheatreById(id);
        theatreRepository.delete(theatre);
    }
    public boolean exists(int id) {
        return theatreRepository.existsById(id);
    }
}

