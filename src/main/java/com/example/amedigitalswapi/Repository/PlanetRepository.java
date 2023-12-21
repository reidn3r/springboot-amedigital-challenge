package com.example.amedigitalswapi.Repository;

import com.example.amedigitalswapi.Domain.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanetRepository extends JpaRepository<Planet, UUID> {
    //query
    Planet findByName(String name);
}
