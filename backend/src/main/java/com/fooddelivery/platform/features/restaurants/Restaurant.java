package com.fooddelivery.platform.features.restaurants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 60)
    private String cuisineType;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false, length = 80)
    private String city;

    @Column(length = 30)
    private String phoneNumber;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Restaurant() {
    }

    public Restaurant(
            String name,
            String description,
            String cuisineType,
            String address,
            String city,
            String phoneNumber,
            String imageUrl,
            boolean active) {
        this.name = name;
        this.description = description;
        this.cuisineType = cuisineType;
        this.address = address;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.active = active;
    }

    @PrePersist
    void setCreatedAt() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void update(
            String name,
            String description,
            String cuisineType,
            String address,
            String city,
            String phoneNumber,
            String imageUrl,
            boolean active) {
        this.name = name;
        this.description = description;
        this.cuisineType = cuisineType;
        this.address = address;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.active = active;
    }
}
