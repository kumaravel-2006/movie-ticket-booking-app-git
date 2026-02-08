package com.moviebooking.service;

import com.moviebooking.entity.Booking;
import com.moviebooking.entity.Theatre;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.repository.BookingRepository;
import com.moviebooking.repository.TheatreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TheatreRepository theatreRepository;

    @InjectMocks
    private BookingService bookingService;

    private Theatre theatre;
    private Booking booking;

    @BeforeEach
    public void setup() {
        theatre = new Theatre();
        theatre.setId(1);
        theatre.setName("Test Theatre");
        theatre.setAvailableSeats(100);

        booking = new Booking();
        booking.setUserName("User");
        booking.setNumberOfTickets(5);
        // mocking the structure passed
        // Wait, since I don't have partial classes, I just set the theatre object
        // but typically from JSON input it might be a shell object with just ID.
        Theatre theatreShell = new Theatre();
        theatreShell.setId(1);
        booking.setTheatre(theatreShell);
    }

    @Test
    public void testAddBooking_Success() {
        when(theatreRepository.findById(1)).thenReturn(Optional.of(theatre));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking created = bookingService.addBooking(booking);

        assertNotNull(created);
        assertEquals(95, theatre.getAvailableSeats()); // Seats deducted
        verify(theatreRepository, times(1)).save(theatre);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    public void testAddBooking_NotEnoughSeats() {
        theatre.setAvailableSeats(2); // Only 2 seats left
        booking.setNumberOfTickets(5); // Requesting 5

        when(theatreRepository.findById(1)).thenReturn(Optional.of(theatre));

        assertThrows(BadRequestException.class, () -> {
            bookingService.addBooking(booking);
        });

        assertEquals(2, theatre.getAvailableSeats()); // Seats NOT deducted
        verify(theatreRepository, never()).save(theatre);
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    public void testAddBooking_TheatreNotFound() {
        when(theatreRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.addBooking(booking);
        });
    }
}
