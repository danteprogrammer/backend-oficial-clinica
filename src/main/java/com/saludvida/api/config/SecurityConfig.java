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

                                                // Rutas de RECEPCIONISTA
                                                .requestMatchers("/api/pacientes/**").hasAuthority("RECEPCIONISTA")
                                                .requestMatchers("/api/citas/**").hasAuthority("RECEPCIONISTA")
                                                .requestMatchers("/api/turnos/**").hasAuthority("RECEPCIONISTA")
                                                .requestMatchers("/api/medicos/especialidades",
                                                                "/api/medicos/especialidad/**",
                                                                "/api/medicos/**/horario")
                                                .hasAuthority("RECEPCIONISTA")
                                                .requestMatchers("/api/historias/paciente/**")
                                                .hasAnyAuthority("RECEPCIONISTA", "MEDICO", "TRIAJE") // Permitir a
                                                                                                      // Recepcionista
                                                                                                      // buscar
                                                                                                      // historias

                                                // Rutas de CAJA
                                                .requestMatchers("/api/facturacion/**").hasAuthority("CAJA")
                                                .requestMatchers("/api/seguros/**").hasAuthority("CAJA")

                                                // Rutas de TRIAJE (¡NUEVO!)
                                                .requestMatchers("/api/triajes/**").hasAuthority("TRIAJE")

                                                // Rutas de MEDICO
                                                .requestMatchers("/api/consultas/**").hasAuthority("MEDICO")

                                                // Rutas de ADMIN (Gestión futura)
                                                .requestMatchers("/api/consultorios/**").hasAuthority("ADMIN")
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
