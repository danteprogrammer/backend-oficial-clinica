package com.saludvida.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor 
public class EspecialidadConteoDto {
    private String especialidad;
    private Long cantidad;
}
