package com.saludvida.api.dto;

import lombok.Data;

// DTO para recibir la solicitud de creación de un horario
@Data
public class HorarioRequestDto {
    private Integer idMedico;
    private String diaSemana;
    private String horaInicio; // Espera formato "HH:mm"
    private String horaFin; // Espera formato "HH:mm"
}