package com.example.amedigitalswapi.Controllers;


import com.example.amedigitalswapi.DTO.DeletePlanetDTO;
import com.example.amedigitalswapi.DTO.PlanetDTO;
import com.example.amedigitalswapi.Domain.Planet;
import com.example.amedigitalswapi.Services.ControllerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PlanetController {
    @Autowired
    ControllerService controllerService;

    @PostMapping("/add")
    public ResponseEntity<Planet> AddPlanet(@RequestBody @Valid PlanetDTO data) throws Exception{
        return this.controllerService.AddPlanet(data);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Planet>> ListAll() throws Exception{
        return this.controllerService.FindAll();
    }

    @GetMapping("/planet/name")
    public ResponseEntity<Planet> GetPlanetByName(@RequestParam(name="name") String name){
        return this.controllerService.GetByName(name);
    }

    @GetMapping("/planet/id")
    public ResponseEntity<Optional<Planet>> GetPlanetById(@RequestParam(name="id") UUID id){
        return this.controllerService.GetByID(id);
    }

    @DeleteMapping("/planet/delete")
    public ResponseEntity<Object> DeleteById(@RequestBody @Valid DeletePlanetDTO data) throws Exception{
        return this.controllerService.DeletePlanetById(data);
    }

}
