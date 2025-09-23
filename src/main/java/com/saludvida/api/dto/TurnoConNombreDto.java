package com.saludvida.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TurnoConNombreDto {
    private String pacienteNombre; // Nombre completo del paciente (nombres + apellidos)
    private Integer consultorioId;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private String observaciones;
}
