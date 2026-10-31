package com.fooddelivery.platform.features.menu;

import com.fooddelivery.platform.features.restaurants.Restaurant;
import com.fooddelivery.platform.features.restaurants.RestaurantService;
import com.fooddelivery.platform.shared.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantService restaurantService;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantService restaurantService) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantService = restaurantService;
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponse> getMenu(Long restaurantId, boolean includeUnavailable) {
        restaurantService.getRestaurant(restaurantId);
        List<MenuItem> items = includeUnavailable
                ? menuItemRepository.findByRestaurantIdOrderByCategoryAscNameAsc(restaurantId)
                : menuItemRepository.findByRestaurantIdAndAvailableTrueOrderByCategoryAscNameAsc(restaurantId);
        return items.stream().map(MenuItemResponse::from).toList();
    }

    @Transactional
    public MenuItemResponse createMenuItem(Long restaurantId, MenuItemRequest request) {
        Restaurant restaurant = restaurantService.requireRestaurant(restaurantId);
        MenuItem item = new MenuItem(
                restaurant,
                request.name().trim(),
                trimToNull(request.description()),
                request.price(),
                request.category().trim(),
                trimToNull(request.imageUrl()),
                request.vegetarian(),
                request.available() == null || request.available());
        return MenuItemResponse.from(menuItemRepository.save(item));
    }

    @Transactional
    public MenuItemResponse updateMenuItem(Long id, MenuItemRequest request) {
        MenuItem item = requireMenuItem(id);
        item.update(
                request.name().trim(),
                trimToNull(request.description()),
                request.price(),
                request.category().trim(),
                trimToNull(request.imageUrl()),
                request.vegetarian(),
                request.available() == null || request.available());
        return MenuItemResponse.from(item);
    }

    @Transactional
    public void deleteMenuItem(Long id) {
        menuItemRepository.delete(requireMenuItem(id));
    }

    private MenuItem requireMenuItem(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item", id));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
