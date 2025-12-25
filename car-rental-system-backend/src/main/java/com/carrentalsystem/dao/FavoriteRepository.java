package com.carrentalsystem.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carrentalsystem.entity.Favorite;
import com.carrentalsystem.entity.User;
import com.carrentalsystem.entity.Variant;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    List<Favorite> findByCustomer(User customer);

    Optional<Favorite> findByCarAndCustomer(Variant car, User customer);

}
