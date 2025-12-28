package com.carrentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.carrentalsystem.dao.BookingDao;
import com.carrentalsystem.entity.Booking;
import com.carrentalsystem.entity.User;
import com.carrentalsystem.entity.Vehicle;
import com.carrentalsystem.utility.Constants.BookingStatus;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingDao bookingDao;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking sampleBooking;
    private User sampleCustomer;

    @BeforeEach
    void setUp() {
        sampleCustomer = new User();
        sampleCustomer.setId(1);
        sampleCustomer.setEmailId("customer@example.com");

        sampleBooking = new Booking();
        sampleBooking.setId(101);
        sampleBooking.setBookingId("BKG12345");
        sampleBooking.setCustomer(sampleCustomer);
        sampleBooking.setStatus(BookingStatus.PENDING.value());
        sampleBooking.setTotalPrice(BigDecimal.valueOf(100.00));
    }

    @Test
    void testAddBooking() {
        when(bookingDao.save(any(Booking.class))).thenReturn(sampleBooking);

        Booking createdBooking = bookingService.addBooking(sampleBooking);

        assertNotNull(createdBooking);
        assertEquals("BKG12345", createdBooking.getBookingId());
        assertEquals(BookingStatus.PENDING.value(), createdBooking.getStatus());
    }

    @Test
    void testGetBookingById_Found() {
        when(bookingDao.findById(101)).thenReturn(Optional.of(sampleBooking));

        Booking foundBooking = bookingService.getById(101);

        assertNotNull(foundBooking);
        assertEquals(101, foundBooking.getId());
    }

    @Test
    void testGetBookingById_NotFound() {
        when(bookingDao.findById(999)).thenReturn(Optional.empty());

        Booking foundBooking = bookingService.getById(999);

        // Expected to return null based on current implementation
        org.junit.jupiter.api.Assertions.assertNull(foundBooking);
    }

    @Test
    void testGetBookingsByCustomer() {
        when(bookingDao.findByCustomer(any(User.class))).thenReturn(Collections.singletonList(sampleBooking));

        List<Booking> bookings = bookingService.getByCustomer(sampleCustomer);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals("BKG12345", bookings.get(0).getBookingId());
    }
}
