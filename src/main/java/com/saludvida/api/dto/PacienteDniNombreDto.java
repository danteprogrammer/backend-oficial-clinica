package com.saludvida.api.dto;

import com.saludvida.api.model.Paciente;
import lombok.Data;

@Data
public class PacienteDniNombreDto {
    private String nombres;
    private String apellidos;
    private String dni;

    public PacienteDniNombreDto(Paciente paciente) {
        this.nombres = paciente.getNombres();
        this.apellidos = paciente.getApellidos();
        this.dni = paciente.getDni();
    }
}