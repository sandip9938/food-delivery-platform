package com.fooddelivery.platform.features.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantIdAndAvailableTrueOrderByCategoryAscNameAsc(Long restaurantId);

    List<MenuItem> findByRestaurantIdOrderByCategoryAscNameAsc(Long restaurantId);

    void deleteByRestaurantId(Long restaurantId);
}
