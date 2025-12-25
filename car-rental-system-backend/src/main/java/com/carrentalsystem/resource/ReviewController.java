package com.carrentalsystem.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrentalsystem.dto.CreateReviewRequest;
import com.carrentalsystem.dto.ReviewResponse;
import com.carrentalsystem.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/review")
@CrossOrigin(origins = "http://localhost:3000") // Allow React frontend
@RequiredArgsConstructor
public class ReviewController {

    @Autowired
    private final ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<ReviewResponse> addReview(@RequestBody CreateReviewRequest request) {
        ReviewResponse response = reviewService.addReview(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fetch/car-wise")
    public ResponseEntity<List<ReviewResponse>> getAllReviewsByCarId(@RequestParam("carId") int carId) {
        List<ReviewResponse> responses = reviewService.getAllReviewsByCarId(carId);
        return ResponseEntity.ok(responses);
    }

}
