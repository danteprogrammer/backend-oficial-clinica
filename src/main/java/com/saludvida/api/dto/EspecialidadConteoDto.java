package com.saludvida.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Constructor para facilitar la creación en la query
public class EspecialidadConteoDto {
    private String especialidad;
    private Long cantidad;
}
