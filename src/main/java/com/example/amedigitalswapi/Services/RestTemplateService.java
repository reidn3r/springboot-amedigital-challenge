package com.example.amedigitalswapi.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService {
    public RestTemplate NewRestTemplateInstance(){
        return new RestTemplate();
    }
}
