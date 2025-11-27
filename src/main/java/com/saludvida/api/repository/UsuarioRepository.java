package com.saludvida.api.repository;

import com.saludvida.api.model.Medico;
import com.saludvida.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByMedico(Medico medico);
}