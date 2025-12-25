package com.carrentalsystem.service;

import java.util.List;

import com.carrentalsystem.dto.CreateReviewRequest;
import com.carrentalsystem.dto.ReviewResponse;

public interface ReviewService {

    ReviewResponse addReview(CreateReviewRequest request);

    List<ReviewResponse> getAllReviewsByCarId(int carId);

}
