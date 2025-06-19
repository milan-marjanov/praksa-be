package com.example.thesimpleeventapp.repository;

import com.example.thesimpleeventapp.model.RestaurantOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantOptionRepository extends JpaRepository<RestaurantOption, Long> {
}
