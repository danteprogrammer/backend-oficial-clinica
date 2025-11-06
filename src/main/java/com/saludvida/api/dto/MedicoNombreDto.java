package com.saludvida.api.dto;

import com.saludvida.api.model.Medico;
import lombok.Data;

@Data
public class MedicoNombreDto {
    private String nombres;
    private String apellidos;

    public MedicoNombreDto(Medico medico) {
        this.nombres = medico.getNombres();
        this.apellidos = medico.getApellidos();
    }
}