package com.example.amedigitalswapi.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class APIPageDTO implements Serializable {
    private String count;
    private String next;
    private List<ResultsDTO> results;

    public APIPageDTO(String count, String next, List<ResultsDTO> results){
        this.count = count;
        this.next = next;
        this.results = results;
    }
}
