package com.fooddelivery.platform.features.menu;

import com.fooddelivery.platform.features.restaurants.Restaurant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 60)
    private String category;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private boolean vegetarian;

    @Column(nullable = false)
    private boolean available = true;

    protected MenuItem() {
    }

    public MenuItem(
            Restaurant restaurant,
            String name,
            String description,
            BigDecimal price,
            String category,
            String imageUrl,
            boolean vegetarian,
            boolean available) {
        this.restaurant = restaurant;
        update(name, description, price, category, imageUrl, vegetarian, available);
    }

    public void update(
            String name,
            String description,
            BigDecimal price,
            String category,
            String imageUrl,
            boolean vegetarian,
            boolean available) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.vegetarian = vegetarian;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public boolean isAvailable() {
        return available;
    }
}
