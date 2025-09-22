package com.saludvida.api.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TurnoDto {
    private Integer pacienteId;
    private Integer consultorioId;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private String observaciones;
    private String estado;
}