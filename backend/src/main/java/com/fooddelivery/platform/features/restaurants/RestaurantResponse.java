package com.fooddelivery.platform.features.restaurants;

import java.time.Instant;

public record RestaurantResponse(
        Long id,
        String name,
        String description,
        String cuisineType,
        String address,
        String city,
        String phoneNumber,
        String imageUrl,
        boolean active,
        Instant createdAt) {

    static RestaurantResponse from(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getCuisineType(),
                restaurant.getAddress(),
                restaurant.getCity(),
                restaurant.getPhoneNumber(),
                restaurant.getImageUrl(),
                restaurant.isActive(),
                restaurant.getCreatedAt());
    }
}
