package com.fooddelivery.platform.features.restaurants;

import com.fooddelivery.platform.features.menu.MenuItemRepository;
import com.fooddelivery.platform.shared.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantService(
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional(readOnly = true)
    public Page<RestaurantResponse> findRestaurants(
            String search, String city, String cuisineType, boolean includeInactive, Pageable pageable) {
        return restaurantRepository.search(
                        normalize(search), normalize(city), normalize(cuisineType), includeInactive, pageable)
                .map(RestaurantResponse::from);
    }

    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurant(Long id) {
        return RestaurantResponse.from(requireRestaurant(id));
    }

    @Transactional
    public RestaurantResponse createRestaurant(RestaurantRequest request) {
        Restaurant restaurant = new Restaurant(
                request.name().trim(),
                trimToNull(request.description()),
                request.cuisineType().trim(),
                request.address().trim(),
                request.city().trim(),
                trimToNull(request.phoneNumber()),
                trimToNull(request.imageUrl()),
                request.active() == null || request.active());
        return RestaurantResponse.from(restaurantRepository.save(restaurant));
    }

    @Transactional
    public RestaurantResponse updateRestaurant(Long id, RestaurantRequest request) {
        Restaurant restaurant = requireRestaurant(id);
        restaurant.update(
                request.name().trim(),
                trimToNull(request.description()),
                request.cuisineType().trim(),
                request.address().trim(),
                request.city().trim(),
                trimToNull(request.phoneNumber()),
                trimToNull(request.imageUrl()),
                request.active() == null || request.active());
        return RestaurantResponse.from(restaurant);
    }

    @Transactional
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = requireRestaurant(id);
        menuItemRepository.deleteByRestaurantId(id);
        restaurantRepository.delete(restaurant);
    }

    public Restaurant requireRestaurant(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
