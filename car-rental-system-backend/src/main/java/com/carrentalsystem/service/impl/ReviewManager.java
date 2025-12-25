package com.carrentalsystem.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrentalsystem.dao.ReviewRepository;
import com.carrentalsystem.dao.UserDao;
import com.carrentalsystem.dao.VariantDao;
import com.carrentalsystem.dto.CreateReviewRequest;
import com.carrentalsystem.dto.ReviewResponse;
import com.carrentalsystem.entity.Review;
import com.carrentalsystem.entity.User;
import com.carrentalsystem.entity.Variant;
import com.carrentalsystem.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewManager implements ReviewService {

    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private final VariantDao variantDao;

    @Autowired
    private final UserDao userDao;

    @Override
    public ReviewResponse addReview(CreateReviewRequest request) {
        Variant car = variantDao.findById(request.getCarId()).orElseThrow(() -> new RuntimeException("Car not found"));
        User user = userDao.findById(request.getCustomerId()).orElseThrow(() -> new RuntimeException("User not found"));

        Review review = Review.builder()
                .comment(request.getComment())
                .rating(request.getRating())
                .reviewDate(LocalDate.now())
                .car(car)
                .customer(user)
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewResponse.builder()
                .id(savedReview.getId())
                .comment(savedReview.getComment())
                .rating(savedReview.getRating())
                .reviewDate(savedReview.getReviewDate())
                .customerName(user.getFirstName() + " " + user.getLastName())
                .customerId(user.getId())
                .carId(car.getId())
                .build();
    }

    @Override
    public List<ReviewResponse> getAllReviewsByCarId(int carId) {
        Variant car = variantDao.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
        List<Review> reviews = reviewRepository.findByCar(car);

        List<ReviewResponse> responses = new ArrayList<>();
        for (Review r : reviews) {
            responses.add(ReviewResponse.builder()
                    .id(r.getId())
                    .comment(r.getComment())
                    .rating(r.getRating())
                    .reviewDate(r.getReviewDate())
                    .customerName(r.getCustomer().getFirstName() + " " + r.getCustomer().getLastName())
                    .customerId(r.getCustomer().getId())
                    .carId(r.getCar().getId())
                    .build());
        }

        return responses;
    }

}
