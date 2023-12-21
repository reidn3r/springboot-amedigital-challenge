package com.example.amedigitalswapi.Domain;

import com.example.amedigitalswapi.DTO.PlanetDTO;
import com.example.amedigitalswapi.DTO.ResultsDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="tb_planets")

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String climate;

    private String terrain;

    private Integer times_in_movie;

    public Planet(ResultsDTO data, String climate, String terrain){
        this.name = data.getName();
        this.climate = climate;
        this.terrain = terrain;
        this.times_in_movie = data.getFilms().size();
    }
}
