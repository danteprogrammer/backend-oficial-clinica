package com.saludvida.api.dto;

import com.saludvida.api.model.Paciente;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PacienteUpdateDTO {
    private String nombres;
    private String apellidos;
    private Paciente.Sexo sexo;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String email;
    private String alergias;
    private String antecedentes;
    private String enfermedadesCronicas;
    private String nombreAseguradora;
    private String numeroPoliza;
    private String cobertura;
}
