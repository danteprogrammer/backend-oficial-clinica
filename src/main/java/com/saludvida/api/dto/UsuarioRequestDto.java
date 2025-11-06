package com.saludvida.api.dto;

import lombok.Data;

@Data
public class UsuarioRequestDto {
    private String nombreUsuario;
    private String clave; // Opcional en actualización
    private String nombres;
    private String apellidos;
    private String estado; // "ACTIVO" o "INACTIVO"
    private Integer idRol;
    private Integer idMedico; // Opcional, solo para rol MEDICO
}
