package com.moviebooking.service;

import com.moviebooking.entity.Movie;
import com.moviebooking.entity.Booking;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.repository.BookingRepository;
import com.moviebooking.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(int id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found with id: " + id));
    }

    public Booking addBooking(Booking booking) {

        Movie movie = movieRepository.findById(booking.getMovie().getId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        int requested = booking.getNumberOfTickets();

        if(requested > 16){
            throw new RuntimeException("Maximum 16 tickets only booked at a time");
        }

        if (movie.getAvailableSeats() < requested) {
            throw new RuntimeException("Not enough seats available");
        }

        movie.setAvailableSeats(movie.getAvailableSeats() - requested);
        movieRepository.save(movie);

        return bookingRepository.save(booking);
    }
    @Transactional
    public void cancelBooking(int bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Movie movie = movieRepository.findById(booking.getMovie().getId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movie.setAvailableSeats(
                movie.getAvailableSeats() + booking.getNumberOfTickets()
        );

        movieRepository.save(movie);

        bookingRepository.deleteById(bookingId);
        bookingRepository.flush();
    }
    public boolean exists(int id) {
        return bookingRepository.existsById(id);
    }


}

