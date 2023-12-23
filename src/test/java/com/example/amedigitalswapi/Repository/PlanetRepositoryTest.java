package com.example.amedigitalswapi.Repository;

import com.example.amedigitalswapi.DTO.ResultsDTO;
import com.example.amedigitalswapi.Domain.Planet;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.xml.transform.Result;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PlanetRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    PlanetRepository planetRepository;

    @Test
    @DisplayName("Should get Planet by name successfully from DB")
    void findByNameCase1() {
        String name = "Earth";
        ResultsDTO data = this.createResultsDTO(name);

        this.createPlanet(data, name, "Urban");

        Planet foundPlanet = this.planetRepository.findByName(name);
        assertThat(foundPlanet != null).isTrue();

    }

    @Test
    @DisplayName("Should not find planet by name")
    void findByNameCase2() {
        String name = "Earth";
        Planet foundPlanet = this.planetRepository.findByName(name);
        assertThat(foundPlanet == null).isTrue();
    }

    private void createPlanet(ResultsDTO data, String name, String terrain){
        Planet newPlanet = new Planet(data, name, terrain);
        this.entityManager.persist(newPlanet);
    }

    private ResultsDTO createResultsDTO (String name){
        List<String> films = new ArrayList<>();
        films.add("movie-01");

        ResultsDTO data = new ResultsDTO();
        data.setFilms(films);
        data.setName(name);
        return data;
    }
}