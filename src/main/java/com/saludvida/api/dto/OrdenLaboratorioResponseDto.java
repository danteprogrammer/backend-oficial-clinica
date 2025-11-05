package com.saludvida.api.dto;

import com.saludvida.api.model.OrdenLaboratorio;
import lombok.Data;
import java.time.LocalDate;

@Data
public class OrdenLaboratorioResponseDto {
    private Integer idOrden;
    private LocalDate fechaOrden;
    private OrdenLaboratorio.EstadoOrden estado;
    private String examenesSolicitados;
    private String resultados;
    private LocalDate fechaResultados;
    private HistoriaClinicaConPacienteDto historiaClinica; // DTO Anidado
    private MedicoNombreDto medico; // DTO Anidado

    // Constructor que acepta la entidad OrdenLaboratorio
    public OrdenLaboratorioResponseDto(OrdenLaboratorio orden) {
        this.idOrden = orden.getIdOrden();
        this.fechaOrden = orden.getFechaOrden();
        this.estado = orden.getEstado();
        this.examenesSolicitados = orden.getExamenesSolicitados();
        this.resultados = orden.getResultados();
        this.fechaResultados = orden.getFechaResultados();
        // Creamos los DTOs anidados
        this.historiaClinica = new HistoriaClinicaConPacienteDto(orden.getHistoriaClinica());
        this.medico = new MedicoNombreDto(orden.getMedico());
    }
}