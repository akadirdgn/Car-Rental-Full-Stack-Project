package com.carrentalsystem.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.carrentalsystem.dto.BookingResponse;
import com.carrentalsystem.entity.Booking;
import com.carrentalsystem.entity.User;
import com.carrentalsystem.service.BookingService;
import com.carrentalsystem.service.UserService;
import com.carrentalsystem.service.VariantService;
import com.carrentalsystem.service.VehicleService;
import com.carrentalsystem.service.PaymentService;

@ExtendWith(MockitoExtension.class)
public class BookingResourceTest {

    @InjectMocks
    private BookingResource bookingResource;

    @Mock
    private BookingService bookingService;

    @Mock
    private UserService userService;

    // Mocks required even if unused in simple tests, as they are autowired in
    // Resource
    @Mock
    private VehicleService vehicleService;
    @Mock
    private VariantService variantService;
    @Mock
    private PaymentService paymentService;

    private User sampleCustomer;
    private Booking sampleBooking;

    @BeforeEach
    void setUp() {
        sampleCustomer = new User();
        sampleCustomer.setId(1);
        sampleCustomer.setEmailId("customer@example.com");

        sampleBooking = new Booking();
        sampleBooking.setBookingId("BKG_TEST");
        sampleBooking.setCustomer(sampleCustomer);
        sampleBooking.setTotalPrice(BigDecimal.valueOf(250.00));
    }

    @Test
    void testFetchAllCustomerBookings_Success() {
        when(userService.getUserById(1)).thenReturn(sampleCustomer);
        when(bookingService.getByCustomer(any(User.class))).thenReturn(Collections.singletonList(sampleBooking));

        ResponseEntity<BookingResponse> response = bookingResource.fetchAllCustomerBookings(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getBookings().size());
        assertEquals("BKG_TEST", response.getBody().getBookings().get(0).getBookingId());
    }

    @Test
    void testFetchAllCustomerBookings_CustomerNotFound() {
        when(userService.getUserById(99)).thenReturn(null);

        ResponseEntity<BookingResponse> response = bookingResource.fetchAllCustomerBookings(99);

        // Check if logic returns OK with success false or BAD_REQUEST
        // The implementation returns OK with success=false message
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody().isSuccess());
        assertEquals("Geçersiz istek - müşteri bulunamadı.", response.getBody().getResponseMessage());
    }
}
