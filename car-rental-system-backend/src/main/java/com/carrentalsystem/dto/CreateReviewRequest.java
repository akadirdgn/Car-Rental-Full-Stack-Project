package com.carrentalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewRequest {

    private String comment;

    private int rating;

    private int carId; // Variant ID

    private int customerId; // User ID

}
