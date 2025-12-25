package com.carrentalsystem.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; // PK

	private String comment;

	private int rating; // 1-5 scale

	private LocalDate reviewDate; // defaults to now

	@ManyToOne
	@JoinColumn(name = "variant_id") // Mapping "Car" to "Variant" in this system
	private Variant car;

	@ManyToOne
	@JoinColumn(name = "user_id") // Mapping "Customer" to "User"
	private User customer;

}
