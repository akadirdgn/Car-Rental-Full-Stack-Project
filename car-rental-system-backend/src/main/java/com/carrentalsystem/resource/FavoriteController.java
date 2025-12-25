package com.carrentalsystem.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrentalsystem.dto.CreateFavoriteRequest;
import com.carrentalsystem.entity.Favorite;
import com.carrentalsystem.entity.Variant;
import com.carrentalsystem.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favorite")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class FavoriteController {

    @Autowired
    private final FavoriteService favoriteService;

    @PostMapping("/add")
    public ResponseEntity<String> addToFavorites(@RequestBody CreateFavoriteRequest request) {
        favoriteService.addToFavorites(request);
        return ResponseEntity.ok("Added to favorites");
    }

    @GetMapping("/fetch/user-wise")
    public ResponseEntity<List<Variant>> getFavoritesByCustomer(@RequestParam("customerId") int customerId) {
        List<Variant> favorites = favoriteService.getFavoritesByCustomer(customerId);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkIfFavorited(@RequestParam("carId") int carId,
            @RequestParam("customerId") int customerId) {
        boolean isFavorited = favoriteService.checkIfFavorited(carId, customerId);
        return ResponseEntity.ok(isFavorited);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromFavorites(@RequestParam("carId") int carId,
            @RequestParam("customerId") int customerId) {
        favoriteService.removeFromFavorites(carId, customerId);
        return ResponseEntity.ok("Removed from favorites");
    }

}
