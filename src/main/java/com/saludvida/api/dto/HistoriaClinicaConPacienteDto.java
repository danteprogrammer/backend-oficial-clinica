package com.saludvida.api.dto;

import com.saludvida.api.model.HistoriaClinica;
import lombok.Data;

@Data
public class HistoriaClinicaConPacienteDto {
    private Integer idHistoriaClinica;
    private PacienteDniNombreDto paciente; // Usamos el DTO de Paciente

    // Constructor que acepta la entidad HistoriaClinica
    public HistoriaClinicaConPacienteDto(HistoriaClinica historia) {
        this.idHistoriaClinica = historia.getIdHistoriaClinica();
        this.paciente = new PacienteDniNombreDto(historia.getPaciente());
    }
}