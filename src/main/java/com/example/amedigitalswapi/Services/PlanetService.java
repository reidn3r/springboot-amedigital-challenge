package com.example.amedigitalswapi.Services;

import com.example.amedigitalswapi.Domain.Planet;
import com.example.amedigitalswapi.Repository.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlanetService {
    @Autowired
    private PlanetRepository planetRepository;

    public List<Planet> FindAll(){
        return this.planetRepository.findAll();
    }

    public Planet SavePlanet(Planet data){
        return this.planetRepository.save(data);
    }

    public Planet FindByPlanetName(String name){
        return this.planetRepository.findByName(name);
    }

    public Optional<Planet> FindPlanetById(UUID id){
        return this.planetRepository.findById(id);
    }

    public void DeletePlanetById(UUID id){
        this.planetRepository.deleteById(id);
    }
}
