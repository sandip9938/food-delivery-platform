package com.fooddelivery.platform.features.restaurants;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("""
            select r from Restaurant r
            where (:search is null
                or lower(r.name) like lower(concat('%', :search, '%'))
                or lower(r.cuisineType) like lower(concat('%', :search, '%')))
              and (:city is null or lower(r.city) = lower(:city))
              and (:cuisineType is null or lower(r.cuisineType) = lower(:cuisineType))
              and (:includeInactive = true or r.active = true)
            """)
    Page<Restaurant> search(
            @Param("search") String search,
            @Param("city") String city,
            @Param("cuisineType") String cuisineType,
            @Param("includeInactive") boolean includeInactive,
            Pageable pageable);

    Optional<Restaurant> findByIdAndActiveTrue(Long id);
}
