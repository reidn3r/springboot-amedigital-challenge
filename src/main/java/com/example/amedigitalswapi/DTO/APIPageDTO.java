package com.example.amedigitalswapi.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class APIPageDTO {
    private String count;
    private String next;
    private List<ResultsDTO> results;
}
