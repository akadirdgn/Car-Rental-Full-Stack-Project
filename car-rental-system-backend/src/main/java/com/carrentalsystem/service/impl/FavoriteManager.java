package com.carrentalsystem.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrentalsystem.dao.FavoriteRepository;
import com.carrentalsystem.dao.UserDao;
import com.carrentalsystem.dao.VariantDao;
import com.carrentalsystem.dto.CreateFavoriteRequest;
import com.carrentalsystem.entity.Favorite;
import com.carrentalsystem.entity.User;
import com.carrentalsystem.entity.Variant;
import com.carrentalsystem.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteManager implements FavoriteService {

    @Autowired
    private final FavoriteRepository favoriteRepository;

    @Autowired
    private final VariantDao variantDao;

    @Autowired
    private final UserDao userDao;

    @Override
    public void addToFavorites(CreateFavoriteRequest request) {
        Variant car = variantDao.findById(request.getCarId()).orElseThrow(() -> new RuntimeException("Car not found"));
        User user = userDao.findById(request.getCustomerId()).orElseThrow(() -> new RuntimeException("User not found"));

        // Check duplicates
        if (checkIfFavorited(request.getCarId(), request.getCustomerId())) {
            return; // Already favorited, do nothing or throw exception
        }

        Favorite favorite = Favorite.builder()
                .car(car)
                .customer(user)
                .build();

        favoriteRepository.save(favorite);
    }

    @Override
    public List<Variant> getFavoritesByCustomer(int customerId) {
        User user = userDao.findById(customerId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Favorite> favorites = favoriteRepository.findByCustomer(user);
        List<Variant> variants = new ArrayList<>();
        for (Favorite fav : favorites) {
            variants.add(fav.getCar());
        }
        return variants;
    }

    @Override
    public boolean checkIfFavorited(int carId, int customerId) {
        Variant car = variantDao.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
        User user = userDao.findById(customerId).orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Favorite> fav = favoriteRepository.findByCarAndCustomer(car, user);
        return fav.isPresent();
    }

    @Override
    public void removeFromFavorites(int carId, int customerId) {
        Variant car = variantDao.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
        User user = userDao.findById(customerId).orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Favorite> fav = favoriteRepository.findByCarAndCustomer(car, user);
        if (fav.isPresent()) {
            favoriteRepository.delete(fav.get());
        }
    }

}
