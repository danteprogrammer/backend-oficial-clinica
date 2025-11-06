package com.saludvida.api.service;

import com.saludvida.api.dto.UsuarioRequestDto;
import com.saludvida.api.dto.UsuarioResponseDto;
import com.saludvida.api.model.Medico;
import com.saludvida.api.model.Rol;
import com.saludvida.api.model.Usuario;
import com.saludvida.api.repository.MedicoRepository;
import com.saludvida.api.repository.RolRepository;
import com.saludvida.api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final MedicoRepository medicoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioResponseDto::new) // Usa el constructor del DTO
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    @Transactional
    public UsuarioResponseDto crearUsuario(UsuarioRequestDto dto) {
        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

        Medico medicoAsociado = null;
        if (dto.getIdMedico() != null) {
            medicoAsociado = medicoRepository.findById(dto.getIdMedico())
                    .orElseThrow(() -> new EntityNotFoundException("Perfil de Médico no encontrado"));
        }

        Usuario usuario = Usuario.builder()
                .nombreUsuario(dto.getNombreUsuario())
                .clave(passwordEncoder.encode(dto.getClave()))
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .estado(Usuario.Estado.valueOf(dto.getEstado().toUpperCase()))
                .rol(rol)
                .medico(medicoAsociado)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return new UsuarioResponseDto(guardado);
    }

    @Transactional
    public UsuarioResponseDto actualizarUsuario(Integer id, UsuarioRequestDto dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

        Medico medicoAsociado = null;
        if (dto.getIdMedico() != null) {
            medicoAsociado = medicoRepository.findById(dto.getIdMedico())
                    .orElseThrow(() -> new EntityNotFoundException("Perfil de Médico no encontrado"));
        }

        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEstado(Usuario.Estado.valueOf(dto.getEstado().toUpperCase()));
        usuario.setRol(rol);
        usuario.setMedico(medicoAsociado);

        // Actualizar clave solo si se proporciona una nueva
        if (dto.getClave() != null && !dto.getClave().isEmpty()) {
            usuario.setClave(passwordEncoder.encode(dto.getClave()));
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return new UsuarioResponseDto(actualizado);
    }
}