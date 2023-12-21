package com.example.amedigitalswapi.Services;

import com.example.amedigitalswapi.DTO.APIPageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SWAPIService {
    /* Metodos para interagir com a API externa */
    @Autowired
    RestTemplateService restTemplateService;

    public ResponseEntity<APIPageDTO> GetPlanetPageByIndex(Integer index){
        String URI = String.format("https://swapi.dev/api/planets/?page=%d", index);
        RestTemplate restTemplate = restTemplateService.NewRestTemplateInstance();
        return restTemplate.getForEntity(URI, APIPageDTO.class);
    }
}
