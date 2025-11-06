package com.saludvida.api.dto;

import lombok.Data;

@Data
public class HorarioRequestDto {
    private Integer idMedico;
    private String diaSemana;
    private String horaInicio; 
    private String horaFin;
}