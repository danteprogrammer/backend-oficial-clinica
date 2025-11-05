package com.saludvida.api.dto;

import lombok.Data;

@Data
public class OrdenLabRequestDto {
    private Integer idHistoriaClinica;
    private Integer idMedico;
    private String examenesSolicitados;
}