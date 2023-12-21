package com.example.amedigitalswapi.Services;

import com.example.amedigitalswapi.DTO.APIPageDTO;
import com.example.amedigitalswapi.DTO.DeletePlanetDTO;
import com.example.amedigitalswapi.DTO.PlanetDTO;
import com.example.amedigitalswapi.DTO.ResultsDTO;
import com.example.amedigitalswapi.Domain.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ControllerService {
    @Autowired
    SWAPIService swapiService;

    @Autowired
    PlanetService planetService;

    public ResponseEntity<List<Planet>> FindAll() throws Exception{
        return ResponseEntity.status(HttpStatus.OK).body(planetService.FindAll());
    }

    public ResponseEntity<Planet> AddPlanet(PlanetDTO data) throws Exception{
        /* Verifica se o planeta a ser inserido está salvo em disco */
        Planet foundPlanet = planetService.FindByPlanetName(data.name());
        if(foundPlanet != null) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        /* Verificar se é um planeta válido */
        int index = 1;
        while(index <= 6){
            ResponseEntity<APIPageDTO> response = this.swapiService.GetPlanetPageByIndex(index);
            if(!response.getStatusCode().equals(HttpStatus.OK)){
                throw new Exception("Erro ao consumir API");
            }
            List<ResultsDTO> results = response.getBody().getResults();
            for(var result : results){
                if(result.getName().equals(data.name())){
                    Planet newPlanet = new Planet(result, data.climate(), data.terrain());
                    return ResponseEntity.status(HttpStatus.CREATED).body(planetService.SavePlanet(newPlanet));
                }
            }
            index++;
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public ResponseEntity<Planet> GetByName(String name){
        Planet foundPlanet = this.planetService.FindByPlanetName(name);

        if(foundPlanet == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(foundPlanet);
    }

    public ResponseEntity<Optional<Planet>> GetByID(UUID id){
        Optional<Planet> foundPlanet = this.planetService.FindPlanetById(id);

        if(foundPlanet.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(foundPlanet);
    }

    public ResponseEntity<Object> DeletePlanetById(DeletePlanetDTO data) throws Exception{
        Optional<Planet> foundPlanet = this.planetService.FindPlanetById(data.id());

        if(foundPlanet.isEmpty()) throw new Exception("Planeta nao encontrado.");

        UUID id = data.id();
        this.planetService.DeletePlanetById(data.id());

        return ResponseEntity.status(HttpStatus.OK).body("id: " + id + " deletado");
    }
}
