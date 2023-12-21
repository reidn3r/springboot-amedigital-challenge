package com.example.amedigitalswapi.Services;

import com.example.amedigitalswapi.DTO.APIPageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
public class SWAPIService {
    /* Metodos para interagir com a API externa */
    @Autowired
    RestTemplateService restTemplateService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public APIPageDTO GetPlanetPageByIndex(Integer index) throws Exception{
        String URI = String.format("https://swapi.dev/api/planets/?page=%d", index);
        RestTemplate restTemplate = restTemplateService.NewRestTemplateInstance();

        /* Tenta buscar na memória */
        String MemoSerialObjectKey = "SWAPI-PAGE:" + index;
        APIPageDTO memo = (APIPageDTO) redisTemplate.opsForValue().get(MemoSerialObjectKey);

        /* Se nao for encontrado busca na API e salva em memória*/
        if(memo == null){
            ResponseEntity<APIPageDTO> response = restTemplate.getForEntity(URI, APIPageDTO.class);
            if(!response.getStatusCode().equals(HttpStatus.OK)) throw new Exception("Erro ao consumir API");
            memo = response.getBody();
            redisTemplate.opsForValue().set(MemoSerialObjectKey, memo, Duration.ofMinutes(30));
        }

        /* Retorna o que foi encontrado/buscado */
        return memo;
    }
}
