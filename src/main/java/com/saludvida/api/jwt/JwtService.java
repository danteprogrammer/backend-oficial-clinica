package com.saludvida.api.jwt;

// --- IMPORTACIONES AÑADIDAS POR EL PROFESOR ---
import com.saludvida.api.model.Medico;
import com.saludvida.api.model.Usuario;
// --- FIN DE IMPORTACIONES AÑADIDAS ---

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration:86400000}") // 86400000ms = 24 horas
    private long jwtExpiration;

    public String getToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("authorities", userDetails.getAuthorities());

        if (userDetails instanceof Usuario) {
            Usuario usuario = (Usuario) userDetails;
            extraClaims.put("rol", usuario.getRol().getNombre()); // Añadimos el rol

            // Si el usuario es un médico y está enlazado
            if (usuario.getMedico() != null) {
                Medico medico = usuario.getMedico();
                Map<String, Object> medicoClaims = new HashMap<>();
                medicoClaims.put("id", medico.getIdMedico());
                medicoClaims.put("nombres", medico.getNombres());
                medicoClaims.put("apellidos", medico.getApellidos());
                medicoClaims.put("cmp", medico.getLicenciaMedica());
                medicoClaims.put("sexo", medico.getSexo());

                extraClaims.put("medicoInfo", medicoClaims);
            }
        }

        // Llamamos al nuevo buildToken con la expiración
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    // --- MÉTODO MODIFICADO (Reemplaza tu private getToken, ahora es buildToken)
    // ---
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Ahora usa la variable de expiración
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // --- MÉTODO AÑADIDO (Sobrecarga pedida por el profesor) ---
    private String buildToken(UserDetails userDetails, long expiration) {
        return buildToken(new HashMap<>(), userDetails, expiration);
    }

    // --- EL RESTO DE TUS MÉTODOS SE MANTIENEN IGUAL ---

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}