package com.saludvida.api.service;

import com.saludvida.api.dto.UsuarioRequestDto;
import com.saludvida.api.dto.UsuarioResponseDto;
import com.saludvida.api.model.Medico;
import com.saludvida.api.model.PasswordResetToken;
import com.saludvida.api.model.Rol;
import com.saludvida.api.model.Usuario;
import com.saludvida.api.repository.MedicoRepository;
import com.saludvida.api.repository.PasswordResetTokenRepository;
import com.saludvida.api.repository.RolRepository;
import com.saludvida.api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final MedicoRepository medicoRepository;
    private final PasswordEncoder passwordEncoder;
    
    private final EmailService emailService;
    private final PasswordResetTokenRepository tokenRepository;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioResponseDto::new)
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

            if (medicoAsociado.getEstado() != Medico.Estado.Activo) {
                throw new IllegalStateException(
                        "No se puede crear un usuario para un médico que no esté 'Activo'.");
            }
        }

        String passwordPlano = dto.getClave();
        if (passwordPlano == null || passwordPlano.trim().isEmpty()) {
            passwordPlano = UUID.randomUUID().toString().substring(0, 8);
        }

        Usuario usuario = Usuario.builder()
                .nombreUsuario(dto.getNombreUsuario())
                .clave(passwordEncoder.encode(passwordPlano)) 
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .estado(Usuario.Estado.valueOf(dto.getEstado().toUpperCase()))
                .rol(rol)
                .medico(medicoAsociado)
                .cambioPasswordObligatorio(true) 
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        if (medicoAsociado != null && medicoAsociado.getEmail() != null && !medicoAsociado.getEmail().isEmpty()) {
            emailService.enviarCredenciales(medicoAsociado.getEmail(), dto.getNombreUsuario(), passwordPlano);
        }

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

            if (medicoAsociado.getEstado() != Medico.Estado.Activo && dto.getEstado().equalsIgnoreCase("ACTIVO")) {
                throw new IllegalStateException("No se puede asignar un médico inactivo a un usuario activo.");
            }
        }

        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEstado(Usuario.Estado.valueOf(dto.getEstado().toUpperCase()));
        usuario.setRol(rol);
        usuario.setMedico(medicoAsociado);

        if (dto.getClave() != null && !dto.getClave().isEmpty()) {
            usuario.setClave(passwordEncoder.encode(dto.getClave()));
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return new UsuarioResponseDto(actualizado);
    }

    @Transactional
    public UsuarioResponseDto inactivarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        usuario.setEstado(Usuario.Estado.INACTIVO);
        Usuario actualizado = usuarioRepository.save(usuario);
        return new UsuarioResponseDto(actualizado);
    }


    @Transactional
    public void procesarOlvidoPassword(String email) {
        Optional<Medico> medicoOpt = medicoRepository.findByEmail(email);
        
        Usuario usuario = null;
        if (medicoOpt.isPresent()) {
            usuario = usuarioRepository.findByMedico(medicoOpt.get()).orElse(null);
        }

        if (usuario != null) {
            String token = UUID.randomUUID().toString();
            
            tokenRepository.deleteByUsuario(usuario);

            PasswordResetToken myToken = PasswordResetToken.builder()
                    .token(token)
                    .usuario(usuario)
                    .fechaExpiracion(LocalDateTime.now().plusHours(1))
                    .build();
            
            tokenRepository.save(myToken);
            emailService.enviarEnlaceRecuperacion(email, token);
        }
    }

    @Transactional
    public void cambiarPasswordConToken(String token, String newPassword) {
        PasswordResetToken passToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (passToken.estaExpirado()) {
            throw new RuntimeException("El token ha expirado");
        }

        Usuario usuario = passToken.getUsuario();
        usuario.setClave(passwordEncoder.encode(newPassword));
        usuario.setCambioPasswordObligatorio(false);
        usuarioRepository.save(usuario);

        tokenRepository.delete(passToken);
    }

    @Transactional
    public void cambiarPasswordAutenticado(String username, String newPassword) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        
        usuario.setClave(passwordEncoder.encode(newPassword));
        usuario.setCambioPasswordObligatorio(false);
        usuarioRepository.save(usuario);
    }
}