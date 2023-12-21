package com.example.amedigitalswapi.Services;

import com.example.amedigitalswapi.DTO.APIPageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SWAPIService {
    /* Metodos para interagir com a API externa */
    @Autowired
    RestTemplateService restTemplateService;

    @Cacheable("SWAPI-page")
    public APIPageDTO GetPlanetPageByIndex(Integer index) throws Exception{
        String URI = String.format("https://swapi.dev/api/planets/?page=%d", index);
        RestTemplate restTemplate = restTemplateService.NewRestTemplateInstance();

        ResponseEntity<APIPageDTO> response = restTemplate.getForEntity(URI, APIPageDTO.class);
        if(!response.getStatusCode().equals(HttpStatus.OK)) throw new Exception("Erro ao consumir API");

        return response.getBody();
    }
}
