package com.saludvida.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final AuthenticationProvider authProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(authRequest -> authRequest

                                                // Rutas públicas (Login)
                                                .requestMatchers("/api/auth/**").permitAll()
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                                // --- REGLAS MÁS ESPECÍFICAS (DEBEN IR PRIMERO) ---

                                                // /api/pacientes/activos (Usado por RECEPCIONISTA, TRIAJE y MEDICO)
                                                .requestMatchers("/api/pacientes/activos")
                                                .hasAnyAuthority("RECEPCIONISTA", "TRIAJE", "MEDICO") // <-- MODIFICADO

                                                // /api/medicos/ (Reglas específicas de RECEPCIONISTA)
                                                .requestMatchers("/api/medicos/especialidades")
                                                .hasAuthority("RECEPCIONISTA")
                                                .requestMatchers("/api/medicos/especialidad/**")
                                                .hasAuthority("RECEPCIONISTA")
                                                .requestMatchers("/api/medicos/*/horario").hasAuthority("RECEPCIONISTA")

                                                // /api/historias/ (Usado por varios roles)
                                                .requestMatchers("/api/historias/paciente/**")
                                                .hasAnyAuthority("RECEPCIONISTA", "MEDICO", "TRIAJE")

                                                // --- REGLAS GENERALES POR ROL (Van después de las específicas) ---

                                                // Regla general de /api/pacientes/ para RECEPCIONISTA
                                                .requestMatchers("/api/pacientes/**").hasAuthority("RECEPCIONISTA")

                                                .requestMatchers("/api/citas/**").hasAuthority("RECEPCIONISTA")
                                                .requestMatchers("/api/turnos/**").hasAuthority("RECEPCIONISTA")

                                                .requestMatchers("/api/facturacion/**").hasAuthority("CAJA")
                                                .requestMatchers("/api/seguros/**").hasAuthority("CAJA")

                                                // /api/triajes/ (Usado por TRIAJE y MEDICO)
                                                .requestMatchers("/api/triajes/**").hasAnyAuthority("TRIAJE", "MEDICO") // <--
                                                                                                                        // MODIFICADO

                                                .requestMatchers("/api/consultas/**").hasAuthority("MEDICO")

                                                // --- NUEVAS REGLAS DE LABORATORIO ---
                                                .requestMatchers("/api/laboratorio/ordenar").hasAuthority("MEDICO")
                                                .requestMatchers("/api/laboratorio/historia/**").hasAuthority("MEDICO")
                                                .requestMatchers("/api/laboratorio/pendientes")
                                                .hasAuthority("LABORATORIO")
                                                .requestMatchers("/api/laboratorio/*/estado")
                                                .hasAuthority("LABORATORIO")
                                                .requestMatchers("/api/laboratorio/*/resultados")
                                                .hasAuthority("LABORATORIO")
                                                // --- FIN NUEVAS REGLAS ---

                                                .requestMatchers("/api/dashboard/**").hasAuthority("ADMIN")

                                                // --- REGLAS DE ADMIN (al final) ---
                                                .requestMatchers("/api/consultorios/**").hasAuthority("ADMIN")
                                                // Regla general de /api/medicos/ para ADMIN
                                                .requestMatchers("/api/medicos/**").hasAuthority("ADMIN")

                                                // Cualquier otra ruta requiere autenticación
                                                .anyRequest().authenticated())

                                .sessionManagement(sessionManager -> sessionManager
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authProvider)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
