package com.saludvida.api.dto;

import com.saludvida.api.model.Usuario;
import lombok.Data;

@Data
public class UsuarioResponseDto {
    private Integer idUsuario;
    private String nombreUsuario;
    private String nombres;
    private String apellidos;
    private String email;
    private String estado;
    private String rolNombre;
    private Integer idRol;
    private String medicoAsociado;
    private Integer idMedicoAsociado;

    public UsuarioResponseDto(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.email = usuario.getEmail();

        this.estado = usuario.getEstado().toString();
        this.rolNombre = usuario.getRol().getNombre();
        this.idRol = usuario.getRol().getIdRol();

        if (usuario.getMedico() != null) {
            this.medicoAsociado = "Dr. " + usuario.getMedico().getApellidos();
            this.idMedicoAsociado = usuario.getMedico().getIdMedico();
        } else {
            this.medicoAsociado = "N/A";
            this.idMedicoAsociado = null;
        }
    }
}