package com.example.amedigitalswapi.Services;

import com.example.amedigitalswapi.DTO.APIPageDTO;
import com.example.amedigitalswapi.DTO.DeletePlanetDTO;
import com.example.amedigitalswapi.DTO.PlanetDTO;
import com.example.amedigitalswapi.DTO.ResultsDTO;
import com.example.amedigitalswapi.Domain.Planet;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
class ControllerServiceTest {

    @Mock
    SWAPIService swapiService;

    @Mock
    PlanetService planetService;

    @InjectMocks
    ControllerService controllerService;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should find all planets in DB")
    void findAll() throws Exception{
        ResultsDTO data = this.createResultsDTO("dto-01");
        List<Planet> PlanetList = new ArrayList<>();
        PlanetList.add(this.createPlanetWithResultsDTO(data, "name", "terrain"));

        when(planetService.FindAll()).thenReturn(PlanetList);

        ResponseEntity<List<Planet>> response = controllerService.FindAll();

        assertEquals(response.getBody(), PlanetList);
        assertThat(response.getStatusCode().equals(HttpStatus.OK)).isTrue();
    }

    @Test
    @DisplayName("Should NOT add a new Planet: CONFLICT")
    void addPlanetCase1() throws Exception{
        String name = "Planet Name";
        ResultsDTO data = this.createResultsDTO(name);
        Planet createdPlanet = this.createPlanetWithResultsDTO(data, name, "terrain");

        when(planetService.FindByPlanetName(name)).thenReturn(createdPlanet);

        PlanetDTO dto = new PlanetDTO(name, "climate", "terrain");
        ResponseEntity<Planet> response = controllerService.AddPlanet(dto);

        assertThat(response.getStatusCode().equals(HttpStatus.CONFLICT)).isTrue();
    }

    @Test
    @DisplayName("Should Create new Planet")
    void addPlanetCase2() throws Exception{
        String name = "Planet Name";
        //Dado a ser inserido
        PlanetDTO data = this.createPlanetDTO(name);

        //Nada foi encontrado no banco de dados
        when(planetService.FindByPlanetName(name)).thenReturn(null);

        //Resultados retornados após chamada para a API externa
        APIPageDTO pageDTO = this.createApiPageDto(name);
        List<ResultsDTO> results = pageDTO.getResults();
        when(swapiService.GetPlanetPageByIndex(1)).thenReturn(pageDTO);

        Planet newPlanet = new Planet(results.get(0), data.climate(), data.terrain());
        when(planetService.SavePlanet(newPlanet)).thenReturn(newPlanet);

        ResponseEntity<Planet> response = controllerService.AddPlanet(data);
        assertThat(response.getStatusCode().equals(HttpStatus.CREATED)).isTrue();
    }


    @Test
    @DisplayName("Should find planet by name successfully")
    void getByNameCase1() {
        String name = "Planet name";
        Planet newPlanet = this.createPlanetRandomTimeInMovie();

        when(this.planetService.FindByPlanetName(name)).thenReturn(newPlanet);

        ResponseEntity<Planet> response = this.controllerService.GetByName(name);
        assertThat(response.getStatusCode().equals(HttpStatus.OK)).isTrue();
        assertThat(response.getBody().equals(newPlanet)).isTrue();
    }

    @Test
    @DisplayName("Should NOT find planet by name")
    void getByNameCase2() {
        String name = "Planet name";
        when(this.planetService.FindByPlanetName(name)).thenReturn(null);

        ResponseEntity<Planet> response = this.controllerService.GetByName(name);
        assertThat(response.getStatusCode().equals(HttpStatus.NOT_FOUND)).isTrue();
    }

    @Test
    @DisplayName("Should NOT find planet by id")
    void getByIDCase1() {
        UUID id = UUID.randomUUID();
        Optional<Planet> foundEmpty = Optional.empty();
        when(this.planetService.FindPlanetById(id)).thenReturn(foundEmpty);

        ResponseEntity<Optional<Planet>> response = this.controllerService.GetByID(id);

        assertThat(foundEmpty.isEmpty()).isTrue();
        assertThat(response.getStatusCode().equals(HttpStatus.NOT_FOUND)).isTrue();
    }
    @Test
    @DisplayName("Should find planet by id successfully")
    void getByIDCase2() {
        UUID id = UUID.randomUUID();
        Planet newPlanet = this.createPlanetBasedOnId(id);

        when(this.planetService.FindPlanetById(id)).thenReturn(Optional.of(newPlanet));

        ResponseEntity<Optional<Planet>> response = this.controllerService.GetByID(id);

        assertThat(response.getStatusCode().equals(HttpStatus.OK)).isTrue();

        Optional<Planet> responseBody = response.getBody();
        Optional<Planet> optionalPlanet = Optional.of(newPlanet);
        assertThat(responseBody.equals(optionalPlanet)).isTrue();
    }

    @Test
    @DisplayName("Should delete planet by id successfully")
    void deletePlanetByIdCase1() throws Exception{
        UUID id = UUID.randomUUID();
        Optional<Planet> newPlanet = Optional.of(this.createPlanetBasedOnId(id));

        when(this.planetService.FindPlanetById(id)).thenReturn(newPlanet);
        doNothing().when(this.planetService).DeletePlanetById(id);

        DeletePlanetDTO deleteDTO = this.createDeleteDTO(id);
        ResponseEntity<Object> response = this.controllerService.DeletePlanetById(deleteDTO);

        assertThat(response.getStatusCode().equals(HttpStatus.OK)).isTrue();
        assertEquals(response.getBody(), "id: " + id + " deletado");
    }
    @Test
    @DisplayName("Should delete planet by id successfully")
    void deletePlanetByIdCase2() throws Exception{
        UUID id = UUID.randomUUID();

        when(this.planetService.FindPlanetById(id)).thenReturn(Optional.empty());

        DeletePlanetDTO deleteDTO = this.createDeleteDTO(id);

        Exception e = assertThrows(Exception.class, () -> {
            this.controllerService.DeletePlanetById(deleteDTO);
        });

        assertEquals("Planeta nao encontrado.", e.getMessage());
    }

    private Planet createPlanetWithResultsDTO(ResultsDTO data, String name, String terrain){
        return new Planet(data, name, terrain);
    }

    private Planet createPlanetBasedOnId(UUID id){
        String name = "Planet Name";
        int times = (int) Math.round(Math.random()*100);

        Planet newPlanet = new Planet();
        newPlanet.setId(id);

        newPlanet.setName(name);
        newPlanet.setTerrain("terrain");
        newPlanet.setClimate("climate");
        newPlanet.setTimes_in_movie(times);
        return newPlanet;
    }

    private Planet createPlanetRandomTimeInMovie(){
        String name = "Planet Name";
        int times = (int) Math.round(Math.random()*100);

        Planet newPlanet = new Planet();
        newPlanet.setName(name);
        newPlanet.setTerrain("terrain");
        newPlanet.setClimate("climate");
        newPlanet.setTimes_in_movie(times);
        return newPlanet;
    }

    private ResultsDTO createResultsDTO (String name){
        List<String> films = new ArrayList<>();
        films.add("movie-01");

        ResultsDTO data = new ResultsDTO();
        data.setFilms(films);
        data.setName(name);
        return data;
    }

    private APIPageDTO createApiPageDto(String name){
        List<ResultsDTO> ResultsList = new ArrayList<>();
        ResultsList.add(this.createResultsDTO(name));
        return new APIPageDTO("0", "2", ResultsList);
    }

    private PlanetDTO createPlanetDTO(String name){
        return new PlanetDTO(name, "climate", "terrain");
    }

    private DeletePlanetDTO createDeleteDTO(UUID id){
        return new DeletePlanetDTO(id);
    }
}