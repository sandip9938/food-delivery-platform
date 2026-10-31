package com.fooddelivery.platform.features.menu;

import java.math.BigDecimal;

public record MenuItemResponse(
        Long id,
        Long restaurantId,
        String name,
        String description,
        BigDecimal price,
        String category,
        String imageUrl,
        boolean vegetarian,
        boolean available) {

    static MenuItemResponse from(MenuItem item) {
        return new MenuItemResponse(
                item.getId(),
                item.getRestaurant().getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getCategory(),
                item.getImageUrl(),
                item.isVegetarian(),
                item.isAvailable());
    }
}
