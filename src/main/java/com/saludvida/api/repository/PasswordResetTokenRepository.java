package com.saludvida.api.repository;

import com.saludvida.api.model.PasswordResetToken;
import com.saludvida.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUsuario(Usuario usuario);
    void deleteByUsuario(Usuario usuario);
}
