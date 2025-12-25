package com.carrentalsystem.service;

import java.util.List;

import com.carrentalsystem.dto.CreateFavoriteRequest;
import com.carrentalsystem.entity.Favorite;
import com.carrentalsystem.entity.Variant;

public interface FavoriteService {

    void addToFavorites(CreateFavoriteRequest request);

    List<Variant> getFavoritesByCustomer(int customerId);

    boolean checkIfFavorited(int carId, int customerId);

    void removeFromFavorites(int carId, int customerId);

}
