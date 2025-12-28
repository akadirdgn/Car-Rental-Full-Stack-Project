package com.carrentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.carrentalsystem.dao.ReviewRepository;
import com.carrentalsystem.dao.UserDao;
import com.carrentalsystem.dao.VariantDao;
import com.carrentalsystem.dto.CreateReviewRequest;
import com.carrentalsystem.dto.ReviewResponse;
import com.carrentalsystem.entity.Review;
import com.carrentalsystem.entity.User;
import com.carrentalsystem.entity.Variant;
import com.carrentalsystem.service.impl.ReviewManager;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private VariantDao variantDao;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private ReviewManager reviewService;

    private Review sampleReview;
    private Variant sampleVariant;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleVariant = new Variant();
        sampleVariant.setId(1);
        sampleVariant.setName("Tesla Model S");

        sampleUser = new User();
        sampleUser.setId(100);
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Doe");

        sampleReview = new Review();
        sampleReview.setId(5);
        sampleReview.setCar(sampleVariant);
        sampleReview.setCustomer(sampleUser);
        sampleReview.setComment("Great car!");
        sampleReview.setRating(5);
        sampleReview.setReviewDate(LocalDate.now());
    }

    @Test
    void testAddReview() {
        CreateReviewRequest request = new CreateReviewRequest();
        request.setCarId(1);
        request.setCustomerId(100);
        request.setComment("Great car!");
        request.setRating(5);

        when(variantDao.findById(1)).thenReturn(Optional.of(sampleVariant));
        when(userDao.findById(100)).thenReturn(Optional.of(sampleUser));
        when(reviewRepository.save(any(Review.class))).thenReturn(sampleReview);

        ReviewResponse response = reviewService.addReview(request);

        assertNotNull(response);
        assertEquals("Great car!", response.getComment());
        assertEquals(5, response.getRating());
    }

    @Test
    void testGetAllReviewsByCarId() {
        when(variantDao.findById(1)).thenReturn(Optional.of(sampleVariant));
        when(reviewRepository.findByCar(sampleVariant)).thenReturn(Collections.singletonList(sampleReview));

        List<ReviewResponse> reviews = reviewService.getAllReviewsByCarId(1);

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals("Great car!", reviews.get(0).getComment());
    }
}
