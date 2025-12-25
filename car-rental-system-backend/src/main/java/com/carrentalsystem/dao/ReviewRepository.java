package com.carrentalsystem.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carrentalsystem.entity.Review;
import com.carrentalsystem.entity.Variant;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByCar(Variant car);

}
