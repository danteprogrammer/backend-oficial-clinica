package com.saludvida.api.dto;

import lombok.Data;

@Data
public class TriajeDto {
    private Double peso;
    private Double altura;
    private String presionArterial;
    private Double temperatura;
    private Double saturacionOxigeno;
}
