package com.fooddelivery.platform.features.menu;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/api/restaurants/{restaurantId}/menu-items")
    public List<MenuItemResponse> getMenu(
            @PathVariable @Positive Long restaurantId,
            @RequestParam(defaultValue = "false") boolean includeUnavailable) {
        return menuItemService.getMenu(restaurantId, includeUnavailable);
    }

    @PostMapping("/api/restaurants/{restaurantId}/menu-items")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse createMenuItem(
            @PathVariable @Positive Long restaurantId,
            @Valid @RequestBody MenuItemRequest request) {
        return menuItemService.createMenuItem(restaurantId, request);
    }

    @PutMapping("/api/menu-items/{id}")
    public MenuItemResponse updateMenuItem(
            @PathVariable @Positive Long id,
            @Valid @RequestBody MenuItemRequest request) {
        return menuItemService.updateMenuItem(id, request);
    }

    @DeleteMapping("/api/menu-items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenuItem(@PathVariable @Positive Long id) {
        menuItemService.deleteMenuItem(id);
    }
}
