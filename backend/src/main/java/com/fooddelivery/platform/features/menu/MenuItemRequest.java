package com.fooddelivery.platform.features.menu;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MenuItemRequest(
        @NotBlank @Size(max = 120) String name,
        @Size(max = 500) String description,
        @NotNull @DecimalMin(value = "0.01") BigDecimal price,
        @NotBlank @Size(max = 60) String category,
        @Size(max = 500) String imageUrl,
        boolean vegetarian,
        Boolean available) {
}
