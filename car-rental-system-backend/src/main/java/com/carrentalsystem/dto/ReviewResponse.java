package com.carrentalsystem.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    private int id;

    private String comment;

    private int rating;

    private LocalDate reviewDate;

    private String customerName; // Simplification for frontend

    private int customerId;

    private int carId; // Variant ID

}
