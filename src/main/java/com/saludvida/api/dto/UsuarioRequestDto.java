package com.saludvida.api.dto;

import lombok.Data;

@Data
public class UsuarioRequestDto {
    private String nombreUsuario;
    private String clave; 
    private String nombres;
    private String apellidos;
    private String estado; 
    private Integer idRol;
    private Integer idMedico; 
}
