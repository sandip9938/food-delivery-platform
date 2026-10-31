package com.fooddelivery.platform.config;

import com.fooddelivery.platform.features.menu.MenuItem;
import com.fooddelivery.platform.features.menu.MenuItemRepository;
import com.fooddelivery.platform.features.restaurants.Restaurant;
import com.fooddelivery.platform.features.restaurants.RestaurantRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@Profile("dev")
public class DemoDataInitializer {

    @Bean
    ApplicationRunner loadDemoData(
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository) {
        return args -> {
            if (restaurantRepository.count() > 0) {
                return;
            }

            Restaurant greenBowl = new Restaurant(
                    "Green Bowl Kitchen",
                    "Bright, feel-good bowls made with fresh local produce.",
                    "Healthy",
                    "12 Market Road",
                    "Pune",
                    "+91 9876543210",
                    null,
                    true);
            Restaurant forno = new Restaurant(
                    "Forno & Fig",
                    "Wood-fired comfort food, baked fresh every day.",
                    "Italian",
                    "8 Riverside Walk",
                    "Pune",
                    "+91 9876543211",
                    null,
                    true);
            Restaurant masala = new Restaurant(
                    "Little Masala House",
                    "Family recipes, slow-simmered spices, lots of heart.",
                    "Indian",
                    "24 Garden Lane",
                    "Mumbai",
                    "+91 9876543212",
                    null,
                    true);

            List<Restaurant> restaurants = restaurantRepository.saveAll(List.of(greenBowl, forno, masala));
            menuItemRepository.saveAll(List.of(
                    item(greenBowl, "Avocado Grain Bowl", "Avocado, warm grains, greens and lemon tahini.", "299.00", "Bowls", true),
                    item(greenBowl, "Garden Crunch Salad", "Crisp greens, roasted chickpeas and herby dressing.", "229.00", "Salads", true),
                    item(greenBowl, "Sweet Potato Hummus Plate", "Roasted sweet potato with house-made hummus.", "249.00", "Small plates", true),
                    item(forno, "Margherita Forno", "San Marzano tomato, fior di latte and basil.", "349.00", "Pizza", true),
                    item(forno, "Mushroom Truffle Tagliatelle", "Fresh pasta, wild mushrooms and parmesan.", "429.00", "Pasta", false),
                    item(forno, "Burrata & Peach", "Creamy burrata, ripe peach and toasted sourdough.", "329.00", "Small plates", true),
                    item(masala, "Butter Paneer Bowl", "Creamy tomato masala, paneer and fragrant rice.", "279.00", "Mains", true),
                    item(masala, "Crispy Samosa Chaat", "Golden samosas, bright chutneys and cool yogurt.", "149.00", "Small plates", true),
                    item(masala, "Tandoori Chicken Tikka", "Smoky, spiced chicken with mint yogurt.", "329.00", "Mains", false)
            ));
        };
    }

    private MenuItem item(
            Restaurant restaurant,
            String name,
            String description,
            String price,
            String category,
            boolean vegetarian) {
        return new MenuItem(
                restaurant,
                name,
                description,
                new BigDecimal(price),
                category,
                null,
                vegetarian,
                true);
    }
}
